/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.disarmingfishingbobber;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WitchEntity.class)
public class WitchEntityMixin {
	@ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F", ordinal = 0))
	private float enchancement$disarmWaterBreathing(float value) {
		return isDisabled(Potions.WATER_BREATHING) ? 2 : value;
	}

	@ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F", ordinal = 1))
	private float enchancement$disarmFireResistance(float value) {
		return isDisabled(Potions.FIRE_RESISTANCE) ? 2 : value;
	}

	@ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F", ordinal = 2))
	private float enchancement$disarmHealing(float value) {
		return isDisabled(Potions.HEALING) ? 2 : value;
	}

	@ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F", ordinal = 3))
	private float enchancement$disarmSwiftness(float value) {
		return isDisabled(Potions.SWIFTNESS) ? 2 : value;
	}

	@Unique
	private boolean isDisabled(RegistryEntry<Potion> potion) {
		return ModEntityComponents.DISARMED_WITCH.get(this).isDisabled(potion);
	}
}
