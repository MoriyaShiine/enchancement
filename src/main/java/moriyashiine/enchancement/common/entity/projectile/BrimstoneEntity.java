/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.entity.projectile;

import moriyashiine.enchancement.common.registry.ModEntityTypes;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class BrimstoneEntity extends PersistentProjectileEntity {
	public static final TrackedData<Float> FORCED_PITCH = DataTracker.registerData(BrimstoneEntity.class, TrackedDataHandlerRegistry.FLOAT);
	public static final TrackedData<Float> FORCED_YAW = DataTracker.registerData(BrimstoneEntity.class, TrackedDataHandlerRegistry.FLOAT);

	public float maxY = 0;
	public int ticksExisted = 0;

	private final Set<Entity> killedEntities = new HashSet<>();

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
		while (maxY < 32) {
			maxY++;
			BlockHitResult hitResult = world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
			if (hitResult.getType() == HitResult.Type.BLOCK) {
				break;
			}
			if (!world.isClient) {
				if (ticksExisted == 5 || ticksExisted == 15) {
					if (getOwner() instanceof LivingEntity owner) {
						world.getOtherEntities(owner, Box.from(hitResult.getPos()).expand(0.5)).forEach(entity -> {
							if (entity instanceof LivingEntity living && !living.isDead()) {
								living.damage(DamageSource.arrow(this, owner), (float) (getDamage() * 5));
								if (living.isDead()) {
									killedEntities.add(living);
								}
							}
						});
					}
				}
			}
			start = end;
			end = start.add(getRotationVector());
		}
		if (!world.isClient) {
			if (ticksExisted > 20) {
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
		dataTracker.set(FORCED_PITCH, nbt.getFloat("ForcedPitch"));
		dataTracker.set(FORCED_YAW, nbt.getFloat("ForcedYaw"));
		ticksExisted = nbt.getInt("TicksExisted");
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putFloat("ForcedPitch", getPitch());
		nbt.putFloat("ForcedYaw", getYaw());
		nbt.putInt("TicksExisted", ticksExisted);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
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
}
