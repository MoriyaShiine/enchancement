/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.disabledurability;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.SetDamageLootFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SetDamageLootFunction.class)
public class SetDamageLootFunctionMixin {
	@Inject(method = "process", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDurability(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
		if (EnchancementUtil.shouldBeUnbreakable(stack)) {
			cir.setReturnValue(stack);
		}
	}
}
