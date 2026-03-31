/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantment.dash.client;

import moriyashiine.enchancement.client.EnchancementClient;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableContents.class)
public class TranslatableContentsMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static Object[] enchancement$dash(Object[] args, String key) {
		if (args.length == 0 && key.equals("enchantment.enchancement.dash.desc")) {
			args = new Object[1];
			args[0] = EnchancementClient.ROTATION_BURST_KEYMAPPING.getTranslatedKeyMessage().copy().withStyle(EnchancementClient.ROTATION_BURST_KEYMAPPING.isUnbound() ? ChatFormatting.RED : ChatFormatting.GOLD);
		}
		return args;
	}
}
