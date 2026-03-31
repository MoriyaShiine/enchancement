/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceconsumables;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Consumable.class)
public class ConsumableMixin {
	@ModifyExpressionValue(method = "canConsume", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodProperties;canAlwaysEat()Z"))
	private boolean enchancement$rebalanceConsumables(boolean original, LivingEntity user, ItemStack stack) {
		if (ModConfig.rebalanceConsumables) {
			if (stack.is(Items.GOLDEN_APPLE) || stack.is(Items.ENCHANTED_GOLDEN_APPLE)) {
				return false;
			}
		}
		return original;
	}
}
