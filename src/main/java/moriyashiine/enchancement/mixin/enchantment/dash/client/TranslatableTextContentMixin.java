/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantment.dash.client;

import moriyashiine.enchancement.client.EnchancementClient;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableTextContent.class)
public class TranslatableTextContentMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static Object[] enchancement$dash(Object[] value, String key) {
		if (value.length == 0 && key.equals("enchantment.enchancement.dash.desc")) {
			value = new Object[1];
			value[0] = EnchancementClient.ROTATION_BURST_KEYBINDING.getBoundKeyLocalizedText().copy().formatted(EnchancementClient.ROTATION_BURST_KEYBINDING.isUnbound() ? Formatting.RED : Formatting.GOLD);
		}
		return value;
	}
}
