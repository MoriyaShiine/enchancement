/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.util.accessor.PersistentProjectileEntityAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class PhasingComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PersistentProjectileEntity obj;
	private int phasingLevel = 0;
	private int ticksInAir = 0;
	private double velocityLength = -1;
	private Vec3d freezeVelocity = null;

	public PhasingComponent(PersistentProjectileEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		phasingLevel = tag.getInt("PhasingLevel");
		ticksInAir = tag.getInt("TicksInAir");
		velocityLength = tag.getDouble("VelocityLength");
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putInt("PhasingLevel", phasingLevel);
		tag.putInt("TicksInAir", ticksInAir);
		tag.putDouble("VelocityLength", velocityLength);
	}

	@Override
	public void tick() {
		if (freezeVelocity != null) {
			obj.setVelocity(freezeVelocity);
			freezeVelocity = null;
		}
		if (shouldPhase()) {
			if (++ticksInAir >= 200 || ((PersistentProjectileEntityAccessor) obj).enchancement$inGround()) {
				disablePhasing();
				return;
			}
			LivingEntity closest = null;
			for (LivingEntity living : obj.getWorld().getEntitiesByClass(LivingEntity.class, new Box(obj.getBlockPos()).expand(phasingLevel * 0.5), foundEntity -> foundEntity.isAlive() && EnchancementUtil.shouldHurt(obj.getOwner(), foundEntity))) {
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

	public void sync() {
		ModEntityComponents.PHASING.sync(obj);
	}

	public int getPhasingLevel() {
		return phasingLevel;
	}

	public void setPhasingLevel(int phasingLevel) {
		this.phasingLevel = phasingLevel;
	}

	public boolean shouldPhase() {
		return phasingLevel > 0;
	}

	public double getVelocityLength() {
		return velocityLength;
	}

	public void disablePhasing() {
		setPhasingLevel(0);
		velocityLength = -1;
		freezeVelocity = obj.getVelocity();
		obj.setVelocity(Vec3d.ZERO);
		obj.setNoGravity(false);
	}
}
