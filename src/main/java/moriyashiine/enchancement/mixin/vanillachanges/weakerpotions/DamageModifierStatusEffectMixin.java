/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.weakerpotions;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.DamageModifierStatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageModifierStatusEffect.class)
public class DamageModifierStatusEffectMixin {
	@SuppressWarnings("ConstantConditions")
	@Inject(method = "adjustModifierAmount", at = @At("RETURN"), cancellable = true)
	private void enchancement$weakerPotions(int amplifier, EntityAttributeModifier modifier, CallbackInfoReturnable<Double> cir) {
		if (ModConfig.weakerPotions) {
			if (DamageModifierStatusEffect.class.cast(this) == StatusEffects.STRENGTH) {
				cir.setReturnValue(cir.getReturnValueD() * 1 / 3F);
			} else if (DamageModifierStatusEffect.class.cast(this) == StatusEffects.WEAKNESS) {
				cir.setReturnValue(cir.getReturnValueD() * 1 / 4F);
			}
		}
	}
}
