/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SetEnchantmentsLootFunction.class)
public class SetEnchantmentsLootFunctionMixin {
	@Unique
	private static ItemStack cachedStack = null;

	@Inject(method = "process", at = @At("HEAD"))
	private void enchancement$disableDisallowedEnchantments(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
		cachedStack = stack;
	}

	@ModifyArg(method = "method_32410", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2IntMap;put(Ljava/lang/Object;I)I"))
	private static Object enchancement$disableDisallowedEnchantments(Object value) {
		if (value instanceof Enchantment enchantment && !enchantment.isEnabled(enchantment.getRequiredFeatures())) {
			return EnchancementUtil.getReplacement(enchantment, cachedStack);
		}
		return value;
	}
}
