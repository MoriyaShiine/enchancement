/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.payload.BoostInFluidC2SPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.SubmersionGate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
	public void readData(ValueInput input) {
		shouldBoost = input.getBooleanOr("ShouldBoost", false);
		boost = input.getFloatOr("Boost", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putBoolean("ShouldBoost", shouldBoost);
		output.putFloat("Boost", boost);
	}

	@Override
	public void tick() {
		float boostStrength = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.BOOST_IN_FLUID, obj, 0);
		hasBoost = boostStrength > 0;
		if (hasBoost) {
			if (!obj.slib$isPlayer() && !obj.hasControllingPassenger() && !obj.level().getBlockState(BlockPos.containing(obj.getEyePosition())).isAir() && SLibUtils.isSubmerged(obj, SubmersionGate.ALL)) {
				shouldBoost = true;
			}
			if (shouldBoost) {
				if (canUse(true)) {
					if (shouldAddDeltaMovement()) {
						boost = (float) Mth.clamp(boost + 0.0025, boostStrength * 0.075, boostStrength);
						obj.push(0, boost, 0);
						obj.gameEvent(GameEvent.ENTITY_ACTION);
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
				ParticleOptions bubbleColumn = ParticleTypes.BUBBLE_COLUMN_UP, splash = ParticleTypes.SPLASH, bubble = ParticleTypes.BUBBLE;
				if (SLibUtils.isSubmerged(obj, SubmersionGate.LAVA_ONLY)) {
					bubbleColumn = splash = bubble = ParticleTypes.LAVA;
				} else if (SLibUtils.isSubmerged(obj, SubmersionGate.POWDER_SNOW_ONLY)) {
					bubbleColumn = splash = bubble = ParticleTypes.SNOWFLAKE;
				}
				obj.level().addParticle(bubbleColumn, obj.getX(), obj.getY(), obj.getZ(), 0, 0.04, 0);
				obj.level().addParticle(bubbleColumn, obj.getRandomX(0.5), obj.getY() + obj.getBbHeight() / 8, obj.getRandomZ(0.5), 0, 0.04, 0);
				if (obj.level().getBlockState(obj.blockPosition().above()).isAir()) {
					for (int i = 0; i < 2; i++) {
						obj.level().addParticle(splash, obj.getRandomX(0.5), obj.getBlockY() + 1, obj.getRandomZ(0.5), 0, 1, 0);
						obj.level().addParticle(bubble, obj.getRandomX(0.5), obj.getBlockY() + 1, obj.getRandomZ(0.5), 0, 0.2, 0);
					}
				}
			}
			LivingEntity entity = obj.getControllingPassenger() instanceof Player player ? player : obj;
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

	private boolean shouldAddDeltaMovement() {
		if (obj.getControllingPassenger() instanceof Player) {
			return obj.level().isClientSide();
		}
		return true;
	}
}
