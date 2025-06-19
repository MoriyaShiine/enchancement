/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.payload.BoostInFluidC2SPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.SubmersionGate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.event.GameEvent;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class BoostInFluidComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final LivingEntity obj;
	private boolean shouldBoost = false;
	private float boost = 0;

	private boolean hasBoost = false;

	public BoostInFluidComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ReadView readView) {
		shouldBoost = readView.getBoolean("ShouldBoost", false);
		boost = readView.getFloat("Boost", 0);
	}

	@Override
	public void writeData(WriteView writeView) {
		writeView.putBoolean("ShouldBoost", shouldBoost);
		writeView.putFloat("Boost", boost);
	}

	@Override
	public void tick() {
		float boostStrength = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.BOOST_IN_FLUID, obj, 0);
		hasBoost = boostStrength > 0;
		if (hasBoost) {
			if (shouldBoost) {
				if (canUse(true)) {
					if (shouldAddVelocity()) {
						boost = (float) MathHelper.clamp(boost + 0.0025, boostStrength * 0.075, boostStrength);
						obj.addVelocity(0, boost, 0);
						obj.emitGameEvent(GameEvent.ENTITY_ACTION);
					}
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
				if (SLibUtils.isSubmerged(obj, SubmersionGate.LAVA_ONLY)) {
					bubbleColumn = ParticleTypes.LAVA;
					splash = ParticleTypes.LAVA;
					bubble = ParticleTypes.LAVA;
				} else if (SLibUtils.isSubmerged(obj, SubmersionGate.POWDER_SNOW_ONLY)) {
					bubbleColumn = ParticleTypes.SNOWFLAKE;
					splash = ParticleTypes.SNOWFLAKE;
					bubble = ParticleTypes.SNOWFLAKE;
				}
				obj.getWorld().addParticleClient(bubbleColumn, x, y, z, 0, 0.04, 0);
				obj.getWorld().addParticleClient(bubbleColumn, obj.getParticleX(0.5), y + obj.getHeight() / 8, obj.getParticleZ(0.5), 0, 0.04, 0);
				if (obj.getWorld().getBlockState(obj.getBlockPos().up()).isAir()) {
					for (int i = 0; i < 2; i++) {
						obj.getWorld().addParticleClient(splash, obj.getParticleX(0.5), obj.getBlockY() + 1, obj.getParticleZ(0.5), 0, 1, 0);
						obj.getWorld().addParticleClient(bubble, obj.getParticleX(0.5), obj.getBlockY() + 1, obj.getParticleZ(0.5), 0, 0.2, 0);
					}
				}
			}
			LivingEntity entity = obj.getControllingPassenger() instanceof PlayerEntity player ? player : obj;
			if (SLibClientUtils.isHost(entity)) {
				if (entity.jumping) {
					if (canUse(false)) {
						shouldBoost = true;
						BoostInFluidC2SPayload.send(obj, true);
					}
				} else if (shouldBoost) {
					shouldBoost = false;
					BoostInFluidC2SPayload.send(obj, false);
				}
			}
		}
	}

	public void setShouldBoost(boolean shouldBoost) {
		this.shouldBoost = shouldBoost;
	}

	public boolean isBoosting() {
		return boost > 0;
	}

	public boolean hasBoost() {
		return hasBoost;
	}

	public boolean canUse(boolean ignoreBoost) {
		return (ignoreBoost || !shouldBoost) && SLibUtils.isGroundedOrAirborne(obj, true) && SLibUtils.isSubmerged(obj, SubmersionGate.ALL);
	}

	private boolean shouldAddVelocity() {
		if (obj.getControllingPassenger() instanceof PlayerEntity) {
			return obj.getWorld().isClient;
		}
		return true;
	}
}
