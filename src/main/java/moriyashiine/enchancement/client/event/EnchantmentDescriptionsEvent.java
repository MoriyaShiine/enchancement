/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;

import java.util.List;

public class EnchantmentDescriptionsEvent implements ItemTooltipCallback {
	@Override
	public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> lines) {
		if (enableDescriptions()) {
			EnchantmentHelper.getEnchantments(stack).getEnchantments().forEach(enchantment -> {
				for (int i = 0; i < lines.size(); i++) {
					String translationKey = EnchancementUtil.getTranslationKey(enchantment);
					if (lines.get(i).getContent() instanceof TranslatableTextContent text && text.getKey().equals(translationKey)) {
						MutableText description = Text.translatable(translationKey + ".desc").formatted(Formatting.DARK_GRAY);
						if (!description.getString().isEmpty()) {
							lines.add(i + 1, Text.literal(" - ").formatted(Formatting.GRAY).append(description));
							break;
						}
					}
				}
			});
		}
	}

	private static boolean enableDescriptions() {
		return ModConfig.enchantmentDescriptions && !Enchancement.commonEnchantmentDescriptionsModLoaded;
	}
}
