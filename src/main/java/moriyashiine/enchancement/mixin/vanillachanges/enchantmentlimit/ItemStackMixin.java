/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract ItemEnchantmentsComponent getEnchantments();

	@Inject(method = "addEnchantment", at = @At("HEAD"), cancellable = true)
	private void enchancement$enchantmentLimit(Enchantment enchantment, int level, CallbackInfo ci) {
		if (enchantment == null || EnchancementUtil.limitCheck(false, EnchancementUtil.getNonDefaultEnchantmentsSize((ItemStack) (Object) this, getEnchantments().getSize()) >= ModConfig.enchantmentLimit)) {
			ci.cancel();
		}
	}
}
