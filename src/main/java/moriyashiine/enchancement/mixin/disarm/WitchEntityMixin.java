/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.disarm;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WitchEntity.class)
public class WitchEntityMixin {
	@ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextFloat()F", ordinal = 0))
	private float enchancement$disarmWaterBreathing(float value) {
		return isDisabled(Potions.WATER_BREATHING) ? 2 : value;
	}

	@ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextFloat()F", ordinal = 1))
	private float enchancement$disarmFireResistance(float value) {
		return isDisabled(Potions.FIRE_RESISTANCE) ? 2 : value;
	}

	@ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextFloat()F", ordinal = 2))
	private float enchancement$disarmHealing(float value) {
		return isDisabled(Potions.HEALING) ? 2 : value;
	}

	@ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextFloat()F", ordinal = 3))
	private float enchancement$disarmSwiftness(float value) {
		return isDisabled(Potions.SWIFTNESS) ? 2 : value;
	}

	@Unique
	private boolean isDisabled(Potion potion) {
		return ModEntityComponents.WITCH_DISARM.get(this).isDisabled(potion);
	}
}
