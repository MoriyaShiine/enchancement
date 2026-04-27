/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.payload.BoostInFluidC2SPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.SubmersionGate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.Arrays;

public class BoostInFluidComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final LivingEntity obj;
	private boolean shouldBoost = false;
	private float boost = 0;

	private boolean hasBoost = false;
	@Nullable
	private SubmersionGate currentSubmersion = null;

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
		currentSubmersion = null;
		if (hasBoost) {
			if (!obj.slib$isPlayer() && !obj.hasControllingPassenger()) {
				shouldBoost = !obj.level().getBlockState(BlockPos.containing(obj.getEyePosition())).isAir() && SLibUtils.isSubmerged(obj, SubmersionGate.ALL);
			}
			if (shouldBoost) {
				currentSubmersion = Arrays.stream(SubmersionGate.values()).filter(gate -> gate != SubmersionGate.ALL && SLibUtils.isSubmerged(obj, gate)).findFirst().orElse(null);
				if (canUse(true)) {
					if (shouldAddDeltaMovement()) {
						boolean submerged = SLibUtils.isSubmerged(obj, SubmersionGate.ALL);
						if (!submerged) {
							boostStrength /= 2;
						}
						float targetBoost = submerged ? boostStrength : boostStrength / 4;
						if (boost <= targetBoost) {
							boost = Math.min(targetBoost, boost + boostStrength / 30F);
						} else {
							boost = Math.max(targetBoost, boost - 0.025F);
						}
						obj.setDeltaMovement(obj.getDeltaMovement().x() * (submerged ? 1 : 0.95), Math.max(boost, obj.getDeltaMovement().y()), obj.getDeltaMovement().z() * (submerged ? 1 : 0.95));
						obj.gameEvent(GameEvent.ENTITY_ACTION);
						EnchancementUtil.resetFallDistance(obj);
						if ((obj.tickCount + obj.getId()) % 3 == 0) {
							SoundEvent sound = SoundEvents.POINTED_DRIPSTONE_DRIP_WATER;
							if (currentSubmersion == SubmersionGate.WATER_ONLY) {
								sound = SoundEvents.BUBBLE_POP;
							} else if (currentSubmersion == SubmersionGate.LAVA_ONLY) {
								sound = SoundEvents.LAVA_POP;
							} else if (currentSubmersion == SubmersionGate.POWDER_SNOW_ONLY) {
								sound = SoundEvents.POWDER_SNOW_BREAK;
							}
							SLibUtils.playSound(obj, sound, 1, Mth.nextFloat(obj.getRandom(), 0.9F, 1.1F));
						}
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
				ParticleOptions main = ParticleTypes.SPLASH, secondary = ParticleTypes.SPLASH, tertiary = ParticleTypes.SPLASH;
				if (currentSubmersion == SubmersionGate.WATER_ONLY) {
					main = ParticleTypes.BUBBLE_COLUMN_UP;
					tertiary = ParticleTypes.BUBBLE;
				} else if (currentSubmersion == SubmersionGate.LAVA_ONLY) {
					main = secondary = tertiary = ParticleTypes.LAVA;
				} else if (currentSubmersion == SubmersionGate.POWDER_SNOW_ONLY) {
					main = secondary = tertiary = ParticleTypes.SNOWFLAKE;
				}
				obj.level().addParticle(main, obj.getX(), obj.getY(), obj.getZ(), 0, 0.04, 0);
				obj.level().addParticle(main, obj.getRandomX(0.5), obj.getY() + obj.getBbHeight() / 8, obj.getRandomZ(0.5), 0, 0.04, 0);
				if (obj.level().getBlockState(obj.blockPosition().above()).isAir()) {
					for (int i = 0; i < 2; i++) {
						obj.level().addParticle(secondary, obj.getRandomX(0.5), obj.getBlockY() + 1, obj.getRandomZ(0.5), 0, 1, 0);
						obj.level().addParticle(tertiary, obj.getRandomX(0.5), obj.getBlockY() + 1, obj.getRandomZ(0.5), 0, 0.2, 0);
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
		return (ignoreBoost || !shouldBoost) && SLibUtils.isGroundedOrAirborne(obj, true) && (SLibUtils.isSubmerged(obj, SubmersionGate.ALL) || ModEntityComponents.EXTENDED_WATER_TIME.get(obj).getTicksWet() > 0);
	}

	public boolean blocksAirEffects() {
		return hasBoost() && canUse(true);
	}

	private boolean shouldAddDeltaMovement() {
		SlamComponent slamComponent = ModEntityComponents.SLAM.getNullable(obj);
		if (slamComponent != null && slamComponent.isSlamming()) {
			return false;
		}
		if (obj.getControllingPassenger() instanceof Player) {
			return obj.level().isClientSide();
		}
		return true;
	}
}
