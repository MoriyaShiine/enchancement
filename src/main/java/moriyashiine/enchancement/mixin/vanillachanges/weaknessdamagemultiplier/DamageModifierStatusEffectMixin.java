package moriyashiine.enchancement.mixin.vanillachanges.weaknessdamagemultiplier;

import moriyashiine.enchancement.common.Enchancement;
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
	private void enchancement$weaknessDamageMultiplier(int amplifier, EntityAttributeModifier modifier, CallbackInfoReturnable<Double> cir) {
		double weaknessDamageMultiplier = Enchancement.getConfig().weaknessDamageMultiplier;
		if (weaknessDamageMultiplier != 1 && DamageModifierStatusEffect.class.cast(this) == StatusEffects.WEAKNESS) {
			cir.setReturnValue(cir.getReturnValueD() * weaknessDamageMultiplier);
		}
	}
}
