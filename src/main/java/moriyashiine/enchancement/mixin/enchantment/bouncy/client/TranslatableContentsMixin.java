/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantment.bouncy.client;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableContents.class)
public class TranslatableContentsMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private static String enchancement$bouncy(String key) {
		if (ModConfig.invertedBounce && key.equals("enchantment.enchancement.bouncy.desc")) {
			return key + ".inverted";
		}
		return key;
	}
}
