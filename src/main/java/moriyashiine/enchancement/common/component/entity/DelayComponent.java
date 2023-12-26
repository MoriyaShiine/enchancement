/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class DelayComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PersistentProjectileEntity obj;
	private ItemStack stackShotFrom = null;
	private Vec3d storedVelocity = null;
	private boolean hasDelay = false;
	private float forcedPitch = 0, forcedYaw = 0;
	private float cachedSpeed = 0, cachedDivergence = 0;
	private int ticksFloating = 0;

	public DelayComponent(PersistentProjectileEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		if (tag.contains("StoredVelocity")) {
			NbtCompound storedVelocity = tag.getCompound("StoredVelocity");
			this.storedVelocity = new Vec3d(storedVelocity.getDouble("X"), storedVelocity.getDouble("Y"), storedVelocity.getDouble("Z"));
		} else {
			storedVelocity = null;
		}
		hasDelay = tag.getBoolean("HasDelay");
		ticksFloating = tag.getInt("TicksFloating");
		forcedPitch = tag.getFloat("ForcedPitch");
		forcedYaw = tag.getFloat("ForcedYaw");
		cachedSpeed = tag.getFloat("CachedSpeed");
		cachedDivergence = tag.getFloat("CachedDivergence");
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		if (storedVelocity != null) {
			NbtCompound storedVelocity = new NbtCompound();
			storedVelocity.putDouble("X", this.storedVelocity.getX());
			storedVelocity.putDouble("Y", this.storedVelocity.getY());
			storedVelocity.putDouble("Z", this.storedVelocity.getZ());
			tag.put("StoredVelocity", storedVelocity);
		}
		tag.putBoolean("HasDelay", hasDelay);
		tag.putInt("TicksFloating", ticksFloating);
		tag.putFloat("ForcedPitch", forcedPitch);
		tag.putFloat("ForcedYaw", forcedYaw);
		tag.putFloat("CachedSpeed", cachedSpeed);
		tag.putFloat("CachedDivergence", cachedDivergence);
	}

	@Override
	public void tick() {
		if (hasDelay) {
			obj.setVelocity(Vec3d.ZERO);
			obj.setPitch(forcedPitch);
			obj.setYaw(forcedYaw);
			ticksFloating++;
		}
	}

	@Override
	public void serverTick() {
		if (hasDelay) {
			if (storedVelocity == null) {
				storedVelocity = obj.getVelocity();
				forcedPitch = obj.getPitch();
				forcedYaw = obj.getYaw();
				sync();
			}
			boolean punching = obj.getOwner() instanceof LivingEntity living && living.handSwinging && (living.getMainHandStack() == stackShotFrom || living.getOffHandStack() == stackShotFrom);
			if (ticksFloating > 300 || punching) {
				if (punching && obj.getOwner() instanceof LivingEntity living && living.isSneaking()) {
					HitResult result = ProjectileUtil.getCollision(living, entity -> !entity.isSpectator() && entity.canHit(), 64);
					Vec3d pos;
					if (result instanceof EntityHitResult entityHitResult) {
						pos = entityHitResult.getEntity().getEyePos();
					} else {
						pos = result.getPos();
					}
					obj.setVelocity(pos.getX() - obj.getX(), pos.getY() - obj.getY(), pos.getZ() - obj.getZ(), cachedSpeed, cachedDivergence);
					storedVelocity = obj.getVelocity();
					obj.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, pos);
					forcedPitch = MathHelper.wrapDegrees(obj.getPitch() + 180);
					forcedYaw = MathHelper.wrapDegrees(-(obj.getYaw() + 180));
				}
				obj.setDamage(obj.getDamage() * MathHelper.lerp(Math.min(1, ticksFloating / 60F), 1, 2.5));
				obj.setVelocity(storedVelocity);
				hasDelay = false;
				sync();
			}
		}
		tick();
	}

	public void sync() {
		ModEntityComponents.DELAY.sync(obj);
	}

	public void setStackShotFrom(ItemStack stackShotFrom) {
		this.stackShotFrom = stackShotFrom;
	}

	public void setCachedSpeed(float cachedSpeed) {
		this.cachedSpeed = cachedSpeed;
	}

	public void setCachedDivergence(float cachedDivergence) {
		this.cachedDivergence = cachedDivergence;
	}

	public boolean hasDelay() {
		return hasDelay;
	}

	public void setHasDelay(boolean hasDelay) {
		this.hasDelay = hasDelay;
	}

	public boolean alwaysHurt() {
		return storedVelocity != null;
	}

	public boolean shouldChangeParticles() {
		return ticksFloating >= 60;
	}
}
