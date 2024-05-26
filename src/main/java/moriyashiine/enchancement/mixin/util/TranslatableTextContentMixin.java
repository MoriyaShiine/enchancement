/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.util;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableTextContent.class)
public class TranslatableTextContentMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private static String enchancement$redirectKey(String value) {
		return getRedirect(value);
	}

	@Unique
	private static String getRedirect(String key) {
		return switch (key) {
			case "enchantment.minecraft.channeling.desc" -> ModConfig.rebalanceChanneling ? key + ".redirect" : key;
			case "enchantment.minecraft.fire_aspect.desc" -> ModConfig.rebalanceFireAspect ? key + ".redirect" : key;
			case "enchantment.minecraft.luck_of_the_sea.desc" ->
					ModConfig.luckOfTheSeaHasLure ? key + ".redirect" : key;
			case "advancements.adventure.two_birds_one_arrow.description" ->
					!ModConfig.disallowedEnchantments.contains("enchancement:brimstone") ? key + ".redirect" : key;
			default -> key;
		};
	}
}
