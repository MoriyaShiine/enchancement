/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.rebalancepotions;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.effect.InstantHealthOrDamageStatusEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(InstantHealthOrDamageStatusEffect.class)
public class InstantHealthOrDamageStatusEffectMixin {
	@ModifyArg(method = "applyInstantEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;heal(F)V"))
	private float enchancement$rebalancePotionsInstantHealth(float value) {
		if (ModConfig.rebalancePotions) {
			return value * 3 / 4F;
		}
		return value;
	}

	@ModifyArg(method = "applyInstantEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
	private float enchancement$rebalancePotionsInstantDamage(float value) {
		if (ModConfig.rebalancePotions) {
			return value / 2F;
		}
		return value;
	}

	@ModifyArg(method = "applyUpdateEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;heal(F)V"))
	private float enchancement$rebalancePotionsUpdateHealth(float value) {
		if (ModConfig.rebalancePotions) {
			return value * 3 / 4F;
		}
		return value;
	}

	@ModifyArg(method = "applyUpdateEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
	private float enchancement$rebalancePotionsUpdateDamage(float value) {
		if (ModConfig.rebalancePotions) {
			return value / 2F;
		}
		return value;
	}
}
