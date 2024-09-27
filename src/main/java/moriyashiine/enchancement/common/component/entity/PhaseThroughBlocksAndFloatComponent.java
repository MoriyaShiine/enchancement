/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.util.accessor.PersistentProjectileEntityAccessor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class PhaseThroughBlocksAndFloatComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final ProjectileEntity obj;
	private int maxPhaseBlocks = 0;
	private int ticksInAir = 0;
	private double velocityLength = -1;
	private Vec3d freezeVelocity = null;

	public PhaseThroughBlocksAndFloatComponent(ProjectileEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		maxPhaseBlocks = tag.getInt("MaxPhaseBlocks");
		ticksInAir = tag.getInt("TicksInAir");
		velocityLength = tag.getDouble("VelocityLength");
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putInt("MaxPhaseBlocks", maxPhaseBlocks);
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
				disable();
				return;
			}
			LivingEntity closest = null;
			for (LivingEntity living : obj.getWorld().getEntitiesByClass(LivingEntity.class, new Box(obj.getBlockPos()).expand(maxPhaseBlocks * 0.5), foundEntity -> foundEntity.isAlive() && EnchancementUtil.shouldHurt(obj.getOwner(), foundEntity))) {
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
		ModEntityComponents.PHASE_THROUGH_BLOCKS_AND_FLOAT.sync(obj);
	}

	public int getMaxPhaseBlocks() {
		return maxPhaseBlocks;
	}

	public void setMaxPhaseBlocks(int maxPhaseBlocks) {
		this.maxPhaseBlocks = maxPhaseBlocks;
	}

	public boolean shouldPhase() {
		return maxPhaseBlocks > 0;
	}

	public double getVelocityLength() {
		return velocityLength;
	}

	public void disable() {
		setMaxPhaseBlocks(0);
		velocityLength = -1;
		freezeVelocity = obj.getVelocity();
		obj.setVelocity(Vec3d.ZERO);
		obj.setNoGravity(false);
	}

	public static void maybeSet(LivingEntity user, ItemStack stack, Entity entity) {
		if (entity instanceof PersistentProjectileEntity) {
			float maxPhaseBlocks = 0;
			if (EnchantmentHelper.hasAnyEnchantmentsWith(stack, ModEnchantmentEffectComponentTypes.PHASE_THROUGH_BLOCKS_AND_FLOAT)) {
				maxPhaseBlocks = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.PHASE_THROUGH_BLOCKS_AND_FLOAT, (ServerWorld) user.getWorld(), stack, 0);
			} else if (!(user instanceof PlayerEntity) && EnchancementUtil.hasAnyEnchantmentsWith(user, ModEnchantmentEffectComponentTypes.PHASE_THROUGH_BLOCKS_AND_FLOAT)) {
				for (ItemStack equippedStack : user.getEquippedItems()) {
					maxPhaseBlocks = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.PHASE_THROUGH_BLOCKS_AND_FLOAT, (ServerWorld) user.getWorld(), equippedStack, 0);
				}
			}
			if (maxPhaseBlocks != 0) {
				PhaseThroughBlocksAndFloatComponent phaseThroughBlocksAndFloatComponent = ModEntityComponents.PHASE_THROUGH_BLOCKS_AND_FLOAT.get(entity);
				phaseThroughBlocksAndFloatComponent.setMaxPhaseBlocks(MathHelper.floor(maxPhaseBlocks));
				phaseThroughBlocksAndFloatComponent.sync();
				entity.setNoGravity(true);
			}
		}
	}
}
