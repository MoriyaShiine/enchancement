/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantment.bouncy.client;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableTextContent.class)
public class TranslatableTextContentMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private static String enchancement$bouncy(String key) {
		if (ModConfig.invertedBounce && key.equals("enchantment.enchancement.bouncy.desc")) {
			return key + ".inverted";
		}
		return key;
	}
}
