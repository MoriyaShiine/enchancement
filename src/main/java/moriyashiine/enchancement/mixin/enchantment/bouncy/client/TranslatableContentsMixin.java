/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantment.bouncy.client;

import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableContents.class)
public class TranslatableContentsMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static Object[] enchancement$bouncy(Object[] args, String key) {
		if (args.length == 0 && key.equals("enchantment.enchancement.bouncy.desc")) {
			args = new Object[1];
			args[0] = EnchancementClient.CHARGE_JUMP_KEYMAPPING.getTranslatedKeyMessage().copy().withStyle(EnchancementClient.CHARGE_JUMP_KEYMAPPING.isUnbound() ? ChatFormatting.RED : ChatFormatting.GOLD);
		}
		return args;
	}

	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private static String enchancement$bouncy(String key) {
		if (ModConfig.invertedBounce && key.equals("enchantment.enchancement.bouncy.desc")) {
			return key + ".inverted";
		}
		return key;
	}
}
