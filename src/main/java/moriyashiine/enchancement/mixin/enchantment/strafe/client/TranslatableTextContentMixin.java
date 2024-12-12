/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantment.strafe.client;

import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableTextContent.class)
public class TranslatableTextContentMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private static String enchancement$strafe(String key) {
		if (ModConfig.doublePressDirectionBurst && key.equals("enchantment.enchancement.strafe.desc")) {
			return key + ".double";
		}
		return key;
	}

	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static Object[] enchancement$strafe(Object[] value, String key) {
		if (value.length == 0 && key.startsWith("enchantment.enchancement.strafe.desc")) {
			value = new Object[1];
			value[0] = EnchancementClient.DIRECTION_BURST_KEYBINDING.getBoundKeyLocalizedText().copy().formatted(EnchancementClient.DIRECTION_BURST_KEYBINDING.isUnbound() ? Formatting.RED : Formatting.GOLD);
		}
		return value;
	}
}
