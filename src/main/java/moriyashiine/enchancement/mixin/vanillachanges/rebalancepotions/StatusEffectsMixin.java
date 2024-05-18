/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.rebalancepotions;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(StatusEffects.class)
public class StatusEffectsMixin {
	@ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;addAttributeModifier(Lnet/minecraft/registry/entry/RegistryEntry;Ljava/lang/String;DLnet/minecraft/entity/attribute/EntityAttributeModifier$Operation;)Lnet/minecraft/entity/effect/StatusEffect;", ordinal = 4))
	private static double enchancement$rebalancePotionsStrength(double value) {
		if (ModConfig.rebalancePotions) {
			return 1;
		}
		return value;
	}

	@ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;addAttributeModifier(Lnet/minecraft/registry/entry/RegistryEntry;Ljava/lang/String;DLnet/minecraft/entity/attribute/EntityAttributeModifier$Operation;)Lnet/minecraft/entity/effect/StatusEffect;", ordinal = 6))
	private static double enchancement$rebalancePotionsWeakness(double value) {
		if (ModConfig.rebalancePotions) {
			return 1;
		}
		return value;
	}
}
