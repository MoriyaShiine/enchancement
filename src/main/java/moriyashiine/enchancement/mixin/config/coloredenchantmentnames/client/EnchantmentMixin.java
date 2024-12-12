/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.coloredenchantmentnames.client;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@ModifyArg(method = "getName", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Style;withColor(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/Style;", ordinal = 1))
	private static Formatting enchancement$singleLevelMode(Formatting value) {
		if (ModConfig.coloredEnchantmentNames) {
			return Formatting.GREEN;
		}
		return value;
	}
}
