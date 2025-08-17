/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.disarmingfishingbobber;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.passive.WanderingTraderEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WanderingTraderEntity.class)
public class WanderingTraderEntityMixin {
	@ModifyExpressionValue(method = "method_18067", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isDay()Z"))
	private boolean enchancement$disarmMilk(boolean original) {
		return original && !ModEntityComponents.DISARMED_WANDERING_TRADER.get(this).disarmedMilk();
	}

	@ModifyExpressionValue(method = "method_18068", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isNight()Z"))
	private boolean enchancement$disarmPotion(boolean original) {
		return original && !ModEntityComponents.DISARMED_WANDERING_TRADER.get(this).disarmedPotion();
	}
}
