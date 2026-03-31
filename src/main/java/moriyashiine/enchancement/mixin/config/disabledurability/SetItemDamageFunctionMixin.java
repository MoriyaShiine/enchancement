/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disabledurability;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SetItemDamageFunction.class)
public class SetItemDamageFunctionMixin {
	@Inject(method = "run", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDurability(ItemStack itemStack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
		if (EnchancementUtil.isUnbreakable(itemStack)) {
			cir.setReturnValue(itemStack);
		}
	}
}
