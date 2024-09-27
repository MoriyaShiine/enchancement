/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class EnchantedTridentsHaveLoyalty implements ItemTooltipCallback {
	@Override
	public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> lines) {
		if (ModConfig.enchantedTridentsHaveLoyalty && stack.hasEnchantments() && stack.isIn(ItemTags.TRIDENT_ENCHANTABLE) && stack.contains(ModComponentTypes.TOGGLEABLE_PASSIVE)) {
			MutableText icon = Text.literal("× ");
			Formatting formatting = Formatting.DARK_RED;
			if (stack.get(ModComponentTypes.TOGGLEABLE_PASSIVE)) {
				icon = Text.literal("✔ ");
				formatting = Formatting.DARK_GREEN;
			}
			lines.add(1, icon.append(Text.translatable("tooltip.enchancement.has_loyalty")).formatted(formatting));
		}
	}
}
