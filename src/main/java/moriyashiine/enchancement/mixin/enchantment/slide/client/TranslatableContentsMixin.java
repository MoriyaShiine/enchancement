/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantment.slide.client;

import moriyashiine.enchancement.client.EnchancementClient;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableContents.class)
public class TranslatableContentsMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static Object[] enchancement$slide(Object[] args, String key) {
		if (args.length == 0 && key.equals("enchantment.enchancement.slide.desc")) {
			args = new Object[2];
			args[0] = EnchancementClient.SLIDE_KEYMAPPING.getTranslatedKeyMessage().copy().withStyle(EnchancementClient.SLIDE_KEYMAPPING.isUnbound() ? ChatFormatting.RED : ChatFormatting.GOLD);
			args[1] = EnchancementClient.SLAM_KEYMAPPING.getTranslatedKeyMessage().copy().withStyle(EnchancementClient.SLAM_KEYMAPPING.isUnbound() ? ChatFormatting.RED : ChatFormatting.GOLD);
		}
		return args;
	}
}
