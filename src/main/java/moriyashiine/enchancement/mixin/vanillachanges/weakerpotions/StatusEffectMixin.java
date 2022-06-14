package moriyashiine.enchancement.mixin.vanillachanges.weakerpotions;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(StatusEffect.class)
public class StatusEffectMixin {
	@ModifyArg(method = "applyInstantEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;heal(F)V"))
	private float enchancement$weakerPotionsInstantHealth(float value) {
		if (ModConfig.weakerPotions) {
			return value * 3 / 4F;
		}
		return value;
	}

	@ModifyArg(method = "applyInstantEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
	private float enchancement$weakerPotionsInstantDamage(float value) {
		if (ModConfig.weakerPotions) {
			return value / 2F;
		}
		return value;
	}

	@ModifyArg(method = "applyUpdateEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;heal(F)V", ordinal = 1))
	private float enchancement$weakerPotionsUpdateHealth(float value) {
		if (ModConfig.weakerPotions) {
			return value * 3 / 4F;
		}
		return value;
	}

	@ModifyArg(method = "applyUpdateEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", ordinal = 2))
	private float enchancement$weakerPotionsUpdateDamage(float value) {
		if (ModConfig.weakerPotions) {
			return value / 2F;
		}
		return value;
	}
}
