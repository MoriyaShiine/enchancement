/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments.client.integration.spectrum;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableTextContent.class)
public class TranslatableTextContentMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private static String enchancement$spectrum$replaceDisallowedEnchantments(String key) {
		if (!EnchancementUtil.isEnchantmentAllowed(Identifier.tryParse("minecraft:fortune"))) {
			if (key.equals("item.spectrum.workstaff.message.fortune")) {
				return "item.spectrum.workstaff.message.molten";
			} else if (key.equals("item.spectrum.workstaff.gui.fortune")) {
				return "item.spectrum.workstaff.gui.molten";
			}
		}
		return key;
	}
}
