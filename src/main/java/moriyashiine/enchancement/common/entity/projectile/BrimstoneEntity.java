/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.entity.projectile;

import moriyashiine.enchancement.client.payload.PlayBrimstoneTravelSoundPayload;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.enchancement.common.init.ModDamageTypes;
import moriyashiine.enchancement.common.init.ModEntityTypes;
import moriyashiine.enchancement.common.init.ModParticleTypes;
import moriyashiine.enchancement.common.tag.ModEntityTypeTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
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
import net.minecraft.particle.ParticleEffect;
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
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Set;

public class BrimstoneEntity extends PersistentProjectileEntity {
	public static final ItemStack BRIMSTONE_STACK;
	public static final int DISTANCE_PER_TICK = 6, MAX_TICKS = 42;
	public static boolean ALWAYS_SPAWN_PARTICLES = false;

	static {
		BRIMSTONE_STACK = new ItemStack(Items.LAVA_BUCKET);
		BRIMSTONE_STACK.set(ModComponentTypes.BRIMSTONE_DAMAGE, Integer.MAX_VALUE);
	}

	public static final TrackedData<Float> DAMAGE = DataTracker.registerData(BrimstoneEntity.class, TrackedDataHandlerRegistry.FLOAT);
	public static final TrackedData<Float> FORCED_PITCH = DataTracker.registerData(BrimstoneEntity.class, TrackedDataHandlerRegistry.FLOAT);
	public static final TrackedData<Float> FORCED_YAW = DataTracker.registerData(BrimstoneEntity.class, TrackedDataHandlerRegistry.FLOAT);

	private static final DustParticleEffect PARTICLE = new DustParticleEffect(new Vector3f(1, 0, 0), 1);

	public int distanceTraveled = 0, ticksExisted = 0;

	private final Set<Entity> hitEntities = new HashSet<>();

	public BrimstoneEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
		ignoreCameraFrustum = true;
	}

	public BrimstoneEntity(World world, LivingEntity owner, @Nullable ItemStack shotFrom) {
		super(ModEntityTypes.BRIMSTONE, owner, world, ItemStack.EMPTY, shotFrom);
		setPosition(owner.getX(), owner.getEyeY() - 0.3, owner.getZ());
		ignoreCameraFrustum = true;
	}

	@Override
	protected ItemStack getDefaultItemStack() {
		return ItemStack.EMPTY;
	}

	@Override
	public void tick() {
		if (!getWorld().isClient && ticksExisted == 0) {
			PlayerLookup.all(getWorld().getServer()).forEach(foundPlayer -> PlayBrimstoneTravelSoundPayload.send(foundPlayer, this));
		}
		if (isCritical()) {
			setCritical(false);
		}
		setVelocity(Vec3d.ZERO);
		for (int i = 0; i < DISTANCE_PER_TICK; i++) {
			float min = Math.min(distanceTraveled, ticksExisted);
			if (min > 0 && min == distanceTraveled) {
				discard();
			}
			Vec3d start = getPos().add(getRotationVector().multiply(distanceTraveled - 1)), end = start.add(getRotationVector());
			BlockHitResult hitResult = getWorld().raycast(new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
			if (hitResult.getType() == HitResult.Type.BLOCK) {
				if (getWorld().isClient) {
					addParticles(PARTICLE, hitResult.getPos().getX(), hitResult.getPos().getY(), hitResult.getPos().getZ());
				} else {
					getWorld().emitGameEvent(GameEvent.PROJECTILE_LAND, hitResult.getPos(), GameEvent.Emitter.of(this));
				}
				break;
			} else {
				Entity owner = getOwner();
				getWorld().getOtherEntities(owner, Box.from(hitResult.getPos()).expand(0.5), EntityPredicates.EXCEPT_SPECTATOR.and(entity -> canEntityBeHit(owner, entity))).forEach(entity -> {
					if (getWorld().isClient) {
						addParticles(PARTICLE, entity.getX(), entity.getRandomBodyY(), entity.getZ());
					} else {
						double damage = getDamage();
						if (entity instanceof LivingEntity living) {
							damage *= living.getMaxHealth() / 20F;
						}
						damage *= getDamageMultiplier(distanceTraveled);
						damage = Math.min(50, damage);
						entity.damage(ModDamageTypes.create(getWorld(), ModDamageTypes.BRIMSTONE, this, owner), (float) damage);
						hitEntities.add(entity);
						if (getOwner() instanceof ServerPlayerEntity player && entity instanceof LivingEntity living) {
							Criteria.KILLED_BY_CROSSBOW.trigger(player, Set.of(living));
						}
					}
				});
			}
			distanceTraveled++;
		}
		if (getWorld().isClient) {
			Vec3d particlePos = getPos().add(getRotationVector().multiply(distanceTraveled));
			addParticles(PARTICLE, particlePos.getX(), particlePos.getY(), particlePos.getZ());
			addParticles(ModParticleTypes.BRIMSTONE_BUBBLE, particlePos.getX(), particlePos.getY(), particlePos.getZ());
		} else if (ticksExisted > MAX_TICKS) {
			discard();
		}
		ticksExisted++;
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
		distanceTraveled = nbt.getInt("DistanceTraveled");
		ticksExisted = nbt.getInt("TicksExisted");
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putFloat("Damage", (float) getDamage());
		nbt.putFloat("ForcedPitch", getPitch());
		nbt.putFloat("ForcedYaw", getYaw());
		nbt.putInt("DistanceTraveled", distanceTraveled);
		nbt.putInt("TicksExisted", ticksExisted);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(DAMAGE, 0F);
		builder.add(FORCED_PITCH, 0F);
		builder.add(FORCED_YAW, 0F);
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

	public float getDamageMultiplier(int distanceTraveled) {
		if (distanceTraveled < 16) {
			return MathHelper.lerp(distanceTraveled / 16F, 0.25F, 1);
		}
		return Math.min(2, MathHelper.lerp((distanceTraveled - 16) / 200F, 1F, 2F));
	}

	private void addParticles(ParticleEffect particle, double x, double y, double z) {
		ALWAYS_SPAWN_PARTICLES = true;
		float range = (float) MathHelper.lerp(getDamage() / 12, 0, 0.5F);
		for (int i = 0; i < 8; i++) {
			getWorld().addParticle(particle,
					x + MathHelper.nextFloat(random, -range, range),
					y + MathHelper.nextFloat(random, -range, range),
					z + MathHelper.nextFloat(random, -range, range),
					MathHelper.nextFloat(random, -1, 1),
					MathHelper.nextFloat(random, -1, 1),
					MathHelper.nextFloat(random, -1, 1));
		}
		ALWAYS_SPAWN_PARTICLES = false;
	}

	private boolean canEntityBeHit(Entity owner, Entity entity) {
		if (entity instanceof LivingEntity || entity.getType().isIn(ModEntityTypeTags.BRIMSTONE_HITTABLE)) {
			return !hitEntities.contains(entity) && entity.isAlive() && EnchancementUtil.shouldHurt(owner, entity);
		}
		return false;
	}
}
