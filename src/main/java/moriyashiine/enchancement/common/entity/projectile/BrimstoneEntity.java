/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.entity.projectile;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModDamageTypes;
import moriyashiine.enchancement.common.init.ModEntityTypes;
import moriyashiine.enchancement.common.init.ModTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Set;

public class BrimstoneEntity extends PersistentProjectileEntity {
	public static final ItemStack BRIMSTONE_STACK;

	static {
		BRIMSTONE_STACK = new ItemStack(Items.LAVA_BUCKET);
		BRIMSTONE_STACK.getOrCreateSubNbt(Enchancement.MOD_ID).putBoolean("Brimstone", true);
	}

	public static final TrackedData<Float> DAMAGE = DataTracker.registerData(BrimstoneEntity.class, TrackedDataHandlerRegistry.FLOAT);
	public static final TrackedData<Float> FORCED_PITCH = DataTracker.registerData(BrimstoneEntity.class, TrackedDataHandlerRegistry.FLOAT);
	public static final TrackedData<Float> FORCED_YAW = DataTracker.registerData(BrimstoneEntity.class, TrackedDataHandlerRegistry.FLOAT);

	private static final DustParticleEffect PARTICLE = new DustParticleEffect(new Vector3f(1, 0, 0), 1);

	public float maxY = 0;
	public int ticksExisted = 0;

	private final Set<Entity> hitEntities = new HashSet<>(), killedEntities = new HashSet<>();

	public BrimstoneEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
		ignoreCameraFrustum = true;

	}

	public BrimstoneEntity(World world, LivingEntity owner) {
		super(ModEntityTypes.BRIMSTONE, owner, world);
		setPosition(owner.getX(), owner.getEyeY() - 0.3, owner.getZ());
	}

	@Override
	protected ItemStack asItemStack() {
		return ItemStack.EMPTY;
	}

	@Override
	public void tick() {
		if (isCritical()) {
			setCritical(false);
		}
		setVelocity(Vec3d.ZERO);
		super.tick();
		ticksExisted++;
		maxY = 0;
		Vec3d start = getPos(), end = start.add(getRotationVector());
		while (maxY < 256) {
			maxY++;
			BlockHitResult hitResult = getWorld().raycast(new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
			if (hitResult.getType() == HitResult.Type.BLOCK) {
				if (getWorld().isClient) {
					addParticles(hitResult.getPos().getX(), hitResult.getPos().getY(), hitResult.getPos().getZ());
				}
				break;
			}
			if (ticksExisted == 3) {
				Entity owner = getOwner();
				getWorld().getOtherEntities(owner, Box.from(hitResult.getPos()).expand(0.5), EntityPredicates.EXCEPT_SPECTATOR.and(entity -> canEntityBeHit(owner, entity))).forEach(entity -> {
					if (getWorld().isClient) {
						addParticles(entity.getX(), entity.getRandomBodyY(), entity.getZ());
					} else {
						double damage = getDamage();
						if (entity instanceof LivingEntity living) {
							damage *= living.getMaxHealth() / 20F;
						}
						if (maxY < 16) {
							damage *= MathHelper.lerp(maxY / 16F, 0.25F, 1);
						} else {
							damage *= Math.min(2, MathHelper.lerp((maxY - 16) / 200F, 1F, 2F));
						}
						damage = Math.min(50, damage);
						entity.damage(ModDamageTypes.create(getWorld(), ModDamageTypes.BRIMSTONE, this, owner), (float) damage);
						hitEntities.add(entity);
						if (entity instanceof LivingEntity living && living.isDead()) {
							killedEntities.add(living);
						}
					}
				});
			}
			start = end;
			end = start.add(getRotationVector());
		}
		if (!getWorld().isClient) {
			if (ticksExisted == 3) {
				getWorld().emitGameEvent(GameEvent.PROJECTILE_LAND, end, GameEvent.Emitter.of(this));
			}
			if (ticksExisted > 10) {
				if (getOwner() instanceof ServerPlayerEntity player) {
					Criteria.KILLED_BY_CROSSBOW.trigger(player, killedEntities);
				}
				discard();
			}
		}
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		setDamage(nbt.getFloat("Damage"));
		dataTracker.set(FORCED_PITCH, nbt.getFloat("ForcedPitch"));
		dataTracker.set(FORCED_YAW, nbt.getFloat("ForcedYaw"));
		ticksExisted = nbt.getInt("TicksExisted");
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putFloat("Damage", (float) getDamage());
		nbt.putFloat("ForcedPitch", getPitch());
		nbt.putFloat("ForcedYaw", getYaw());
		nbt.putInt("TicksExisted", ticksExisted);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(DAMAGE, 0F);
		dataTracker.startTracking(FORCED_PITCH, 0F);
		dataTracker.startTracking(FORCED_YAW, 0F);
	}

	@Override
	public float getPitch() {
		return dataTracker.get(FORCED_PITCH);
	}

	@Override
	public float getYaw() {
		return dataTracker.get(FORCED_YAW);
	}

	@Override
	public void setDamage(double damage) {
		dataTracker.set(DAMAGE, (float) damage);
	}

	@Override
	public double getDamage() {
		return dataTracker.get(DAMAGE);
	}

	private void addParticles(double x, double y, double z) {
		float range = (float) MathHelper.lerp(getDamage() / 12, 0, 0.3F);
		for (int i = 0; i < 8; i++) {
			getWorld().addParticle(PARTICLE, x + MathHelper.nextFloat(random, -range, range), y + MathHelper.nextFloat(random, -range, range), z + MathHelper.nextFloat(random, -range, range), MathHelper.nextFloat(random, -1, 1), MathHelper.nextFloat(random, -1, 1), MathHelper.nextFloat(random, -1, 1));
		}
	}

	private boolean canEntityBeHit(Entity owner, Entity entity) {
		if (entity instanceof LivingEntity || entity.getType().isIn(ModTags.EntityTypes.BRIMSTONE_HITTABLE)) {
			return !hitEntities.contains(entity) && entity.isAlive() && EnchancementUtil.shouldHurt(owner, entity);
		}
		return false;
	}
}
