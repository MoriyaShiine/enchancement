/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.enchantmentlimit;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
	public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(type, syncId, playerInventory, context);
	}

	@ModifyExpressionValue(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;canBeCombined(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/registry/entry/RegistryEntry;)Z"))
	private boolean enchancement$enchantmentLimit(boolean value, @Local(ordinal = 0) ItemStack stack) {
		return value && !EnchancementUtil.limitCheck(false, EnchancementUtil.getNonDefaultEnchantmentsSize(stack, stack.getEnchantments().getSize() + 1) > ModConfig.enchantmentLimit);
	}
}
