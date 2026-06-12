/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.disarmingfishingbobber;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WanderingTrader.class)
public class WanderingTraderMixin {
	@ModifyExpressionValue(method = "lambda$registerGoals$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isDarkOutside()Z"))
	private boolean enchancement$disarmPotion(boolean original) {
		return original && !EnchancementEntityComponents.DISARMED_WANDERING_TRADER.get(this).disarmedPotion();
	}

	@ModifyExpressionValue(method = "lambda$registerGoals$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isBrightOutside()Z"))
	private boolean enchancement$disarmMilk(boolean original) {
		return original && !EnchancementEntityComponents.DISARMED_WANDERING_TRADER.get(this).disarmedMilk();
	}
}
