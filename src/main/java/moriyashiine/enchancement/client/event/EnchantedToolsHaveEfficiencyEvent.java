/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModDataComponentTypes;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.TooltipType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class EnchantedToolsHaveEfficiencyEvent implements ItemTooltipCallback {
	@Override
	public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> lines) {
		if (ModConfig.enchantedToolsHaveEfficiency && stack.hasEnchantments() && stack.isIn(ItemTags.MINING_ENCHANTABLE) && stack.contains(ModDataComponentTypes.TOGGLEABLE_PASSIVE)) {
			MutableText icon = Text.literal("× ");
			Formatting formatting = Formatting.DARK_RED;
			if (stack.get(ModDataComponentTypes.TOGGLEABLE_PASSIVE)) {
				icon = Text.literal("✔ ");
				formatting = Formatting.DARK_GREEN;
			}
			lines.add(1, icon.append(Text.translatable("tooltip.enchancement.has_efficiency")).formatted(formatting));
		}
	}
}
