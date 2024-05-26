/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.slide.client;

import moriyashiine.enchancement.client.EnchancementClient;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableTextContent.class)
public class TranslatableTextContentMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static Object[] enchancement$slide(Object[] value, String key) {
		if (value.length == 0 && key.equals("enchantment.enchancement.slide.desc")) {
			value = new Object[2];
			value[0] = EnchancementClient.SLIDE_KEYBINDING.getBoundKeyLocalizedText().copy().formatted(EnchancementClient.SLIDE_KEYBINDING.isUnbound() ? Formatting.RED : Formatting.GOLD);
			value[1] = EnchancementClient.SLAM_KEYBINDING.getBoundKeyLocalizedText().copy().formatted(EnchancementClient.SLAM_KEYBINDING.isUnbound() ? Formatting.RED : Formatting.GOLD);
		}
		return value;
	}
}
