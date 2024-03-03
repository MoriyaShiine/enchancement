/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.util.PersistentProjectileEntityAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.NotNull;

public class PhasingComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PersistentProjectileEntity obj;
	private boolean shouldPhase = false;
	private int ticksInAir = 0;
	private double velocityLength = -1;

	public PhasingComponent(PersistentProjectileEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		shouldPhase = tag.getBoolean("ShouldPhase");
		ticksInAir = tag.getInt("TicksInAir");
		velocityLength = tag.getDouble("VelocityLength");
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
		tag.putBoolean("ShouldPhase", shouldPhase);
		tag.putInt("TicksInAir", ticksInAir);
		tag.putDouble("VelocityLength", velocityLength);
	}

	@Override
	public void tick() {
		if (shouldPhase && !((PersistentProjectileEntityAccessor) obj).enchancement$inGround()) {
			LivingEntity closest = null;
			for (LivingEntity living : obj.getWorld().getEntitiesByClass(LivingEntity.class, new Box(obj.getBlockPos()).expand(1), foundEntity -> foundEntity.isAlive() && EnchancementUtil.shouldHurt(obj.getOwner(), foundEntity))) {
				if (closest == null || closest.distanceTo(obj) > living.distanceTo(obj)) {
					closest = living;
				}
			}
			if (closest != null) {
				if (obj.getWorld().isClient) {
					for (int i = 0; i < 8; i++) {
						obj.getWorld().addParticle(ParticleTypes.PORTAL, obj.getParticleX(0.5), obj.getRandomBodyY(), obj.getParticleZ(0.5), 0, 0, 0);
					}
				} else {
					if (velocityLength == -1) {
						velocityLength = obj.getVelocity().length();
						obj.getWorld().playSound(null, obj.getBlockPos(), ModSoundEvents.ENTITY_GENERIC_TELEPORT, obj.getSoundCategory(), 0.75F, 1);
					}
					obj.setVelocity(closest.getX() - obj.getX(), closest.getEyeY() - obj.getEyeY(), closest.getZ() - obj.getZ());
				}
			}
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (shouldPhase && ticksInAir++ >= 200) {
			shouldPhase = false;
			velocityLength = -1;
			obj.setNoGravity(false);
		}
	}

	public void sync() {
		ModEntityComponents.PHASING.sync(obj);
	}

	public void setShouldPhase(boolean shouldPhase) {
		this.shouldPhase = shouldPhase;
	}

	public boolean shouldPhase() {
		return shouldPhase;
	}

	public double getVelocityLength() {
		return velocityLength;
	}
}
