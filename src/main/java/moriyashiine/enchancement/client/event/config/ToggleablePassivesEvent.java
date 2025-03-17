/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event.config;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToggleablePassivesEvent implements ItemTooltipCallback {
	private static final Map<TagKey<Item>, String> KEY_MAP = new HashMap<>();

	static {
		KEY_MAP.put(ItemTags.CHEST_ARMOR_ENCHANTABLE, "tooltip.enchancement.increases_air_mobility");
		KEY_MAP.put(ItemTags.MINING_ENCHANTABLE, "tooltip.enchancement.has_efficiency");
		KEY_MAP.put(ItemTags.TRIDENT_ENCHANTABLE, "tooltip.enchancement.has_loyalty");
	}

	@Override
	public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> lines) {
		if (ModConfig.toggleablePassives && stack.hasEnchantments() && stack.contains(ModComponentTypes.TOGGLEABLE_PASSIVE)) {
			KEY_MAP.forEach((tag, key) -> {
				if (stack.isIn(tag) || (tag.equals(ItemTags.CHEST_ARMOR_ENCHANTABLE) && stack.getItem() instanceof AnimalArmorItem)) {
					MutableText icon = Text.literal("× ");
					Formatting formatting = Formatting.DARK_RED;
					if (stack.get(ModComponentTypes.TOGGLEABLE_PASSIVE)) {
						icon = Text.literal("✔ ");
						formatting = Formatting.DARK_GREEN;
					}
					lines.add(1, icon.append(Text.translatable(key)).formatted(formatting));
				}
			});
		}
	}
}
