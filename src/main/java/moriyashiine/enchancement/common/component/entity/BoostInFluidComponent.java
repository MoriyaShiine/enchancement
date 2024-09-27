/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.payload.BoostInFluidPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.util.SubmersionGate;
import moriyashiine.enchancement.mixin.util.accessor.LivingEntityAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.MathHelper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class BoostInFluidComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private boolean shouldBoost = false;
	private float boost = 0;

	private boolean hasBoost = false;

	public BoostInFluidComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		shouldBoost = tag.getBoolean("ShouldBoost");
		boost = tag.getFloat("Boost");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("ShouldBoost", shouldBoost);
		tag.putFloat("Boost", boost);
	}

	@Override
	public void tick() {
		float boostStrength = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.BOOST_IN_FLUID, obj, 0);
		hasBoost = boostStrength > 0;
		if (hasBoost) {
			if (shouldBoost) {
				if (EnchancementUtil.isSubmerged(obj, SubmersionGate.ALL) && EnchancementUtil.isGroundedOrAirborne(obj, true)) {
					boost = (float) MathHelper.clamp(boost + 0.0025, boostStrength * 0.075, boostStrength);
					obj.addVelocity(0, boost, 0);
				} else {
					shouldBoost = false;
					boost = 0;
				}
			} else {
				boost = 0;
			}
		} else {
			shouldBoost = false;
			boost = 0;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasBoost) {
			if (shouldBoost) {
				double x = obj.getX();
				double y = obj.getY();
				double z = obj.getZ();
				ParticleEffect bubbleColumn = ParticleTypes.BUBBLE_COLUMN_UP, splash = ParticleTypes.SPLASH, bubble = ParticleTypes.BUBBLE;
				if (EnchancementUtil.isSubmerged(obj, SubmersionGate.LAVA_ONLY)) {
					bubbleColumn = ParticleTypes.LAVA;
					splash = ParticleTypes.LAVA;
					bubble = ParticleTypes.LAVA;
				} else if (EnchancementUtil.isSubmerged(obj, SubmersionGate.POWDER_SNOW_ONLY)) {
					bubbleColumn = ParticleTypes.SNOWFLAKE;
					splash = ParticleTypes.SNOWFLAKE;
					bubble = ParticleTypes.SNOWFLAKE;
				}
				obj.getWorld().addParticle(bubbleColumn, x, y, z, 0, 0.04, 0);
				obj.getWorld().addParticle(bubbleColumn, obj.getParticleX(0.5), y + obj.getHeight() / 8, obj.getParticleZ(0.5), 0, 0.04, 0);
				if (obj.getWorld().getBlockState(obj.getBlockPos().up()).isAir()) {
					for (int i = 0; i < 2; i++) {
						obj.getWorld().addParticle(splash, obj.getParticleX(0.5), obj.getBlockY() + 1, obj.getParticleZ(0.5), 0, 1, 0);
						obj.getWorld().addParticle(bubble, obj.getParticleX(0.5), obj.getBlockY() + 1, obj.getParticleZ(0.5), 0, 0.2, 0);
					}
				}
			}
			if (((LivingEntityAccessor) obj).enchancement$jumping()) {
				if (canUse(false)) {
					shouldBoost = true;
					BoostInFluidPayload.send(true);
				}
			} else if (shouldBoost) {
				shouldBoost = false;
				BoostInFluidPayload.send(false);
			}
		}
	}

	public void setShouldBoost(boolean shouldBoost) {
		this.shouldBoost = shouldBoost;
	}

	public boolean hasBoost() {
		return hasBoost;
	}

	public boolean canUse(boolean ignoreBoost) {
		return (ignoreBoost || !shouldBoost) && EnchancementUtil.isSubmerged(obj, SubmersionGate.ALL);
	}
}
