/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.coloredenchantmentnames.client;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@ModifyArg(method = "getFullname", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Style;withColor(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/Style;", ordinal = 1))
	private static ChatFormatting enchancement$singleLevelMode(ChatFormatting color) {
		if (ModConfig.coloredEnchantmentNames) {
			return ChatFormatting.GREEN;
		}
		return color;
	}
}
