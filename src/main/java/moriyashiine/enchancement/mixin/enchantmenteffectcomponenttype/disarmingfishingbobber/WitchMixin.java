/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.disarmingfishingbobber;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Witch.class)
public class WitchMixin {
	@ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F", ordinal = 0))
	private float enchancement$disarmWaterBreathing(float value) {
		return isDisabled(Potions.WATER_BREATHING) ? 2 : value;
	}

	@ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F", ordinal = 1))
	private float enchancement$disarmFireResistance(float value) {
		return isDisabled(Potions.FIRE_RESISTANCE) ? 2 : value;
	}

	@ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F", ordinal = 2))
	private float enchancement$disarmHealing(float value) {
		return isDisabled(Potions.HEALING) ? 2 : value;
	}

	@ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F", ordinal = 3))
	private float enchancement$disarmSwiftness(float value) {
		return isDisabled(Potions.SWIFTNESS) ? 2 : value;
	}

	@Unique
	private boolean isDisabled(Holder<Potion> potion) {
		return ModEntityComponents.DISARMED_WITCH.get(this).isDisabled(potion);
	}
}
