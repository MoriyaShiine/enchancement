/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableContents.class)
public class TranslatableContentsMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private static String enchancement$redirectKey(String key) {
		return getRedirect(key);
	}

	@Unique
	private static String getRedirect(String key) {
		return switch (key) {
			case "advancements.adventure.two_birds_one_arrow.description" ->
					!ModConfig.disallowedEnchantments.contains("enchancement:brimstone") ? key + ".redirect" : key;
			case "advancements.adventure.overoverkill.description" ->
					ModConfig.rebalanceEquipment ? key + ".redirect" : key;
			case "enchantment.minecraft.channeling.desc", "enchantment.minecraft.luck_of_the_sea.desc",
				 "enchantment.minecraft.fire_aspect.desc", "enchantment.minecraft.wind_burst.desc" ->
					ModConfig.rebalanceEnchantments ? key + ".redirect" : key;
			default -> key;
		};
	}
}
