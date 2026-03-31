/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantment.strafe.client;

import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableContents.class)
public class TranslatableContentsMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private static String enchancement$strafe(String key) {
		if (ModConfig.doublePressDirectionBurst && key.equals("enchantment.enchancement.strafe.desc")) {
			return key + ".double";
		}
		return key;
	}

	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static Object[] enchancement$strafe(Object[] args, String key) {
		if (args.length == 0 && key.startsWith("enchantment.enchancement.strafe.desc")) {
			args = new Object[1];
			args[0] = EnchancementClient.DIRECTION_BURST_KEYMAPPING.getTranslatedKeyMessage().copy().withStyle(EnchancementClient.DIRECTION_BURST_KEYMAPPING.isUnbound() ? ChatFormatting.RED : ChatFormatting.GOLD);
		}
		return args;
	}
}
