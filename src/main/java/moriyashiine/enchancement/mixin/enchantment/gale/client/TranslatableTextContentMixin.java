/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantment.gale.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableTextContent.class)
public class TranslatableTextContentMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static Object[] enchancement$gale(Object[] value, String key) {
		if (value.length == 0 && key.equals("enchantment.enchancement.gale.desc")) {
			value = new Object[1];
			value[0] = MinecraftClient.getInstance().options.jumpKey.getBoundKeyLocalizedText().copy().formatted(MinecraftClient.getInstance().options.jumpKey.isUnbound() ? Formatting.RED : Formatting.GOLD);
		}
		return value;
	}
}
