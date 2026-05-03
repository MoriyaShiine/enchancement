/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.event.config;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToggleablePassivesClientEvent implements ItemTooltipCallback {
	private static final Map<TagKey<Item>, String> KEY_MAP = new HashMap<>();

	static {
		KEY_MAP.put(ItemTags.CHEST_ARMOR_ENCHANTABLE, "tooltip.enchancement.increases_air_mobility");
		KEY_MAP.put(ItemTags.MINING_ENCHANTABLE, "tooltip.enchancement.has_efficiency");
		KEY_MAP.put(ItemTags.TRIDENT_ENCHANTABLE, "tooltip.enchancement.has_loyalty");
	}

	@Override
	public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, List<Component> lines) {
		if (ModConfig.toggleablePassives && stack.isEnchanted() && stack.has(ModComponentTypes.TOGGLEABLE_PASSIVE)) {
			KEY_MAP.forEach((tag, key) -> {
				if (stack.is(tag) || (tag.equals(ItemTags.CHEST_ARMOR_ENCHANTABLE) && EnchancementUtil.isGroundAnimalArmor(stack))) {
					MutableComponent icon = Component.literal("× ");
					ChatFormatting formatting = ChatFormatting.DARK_RED;
					if (stack.get(ModComponentTypes.TOGGLEABLE_PASSIVE)) {
						icon = Component.literal("✔ ");
						formatting = ChatFormatting.DARK_GREEN;
					}
					lines.add(1, icon.append(Component.translatable(key)).withStyle(formatting));
				}
			});
		}
	}
}
