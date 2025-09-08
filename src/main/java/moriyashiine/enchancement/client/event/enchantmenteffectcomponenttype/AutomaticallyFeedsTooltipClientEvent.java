/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.tag.ModEnchantmentTags;
import moriyashiine.enchancement.common.tag.ModItemTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class AutomaticallyFeedsTooltipClientEvent implements ItemTooltipCallback {
	@Override
	public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> lines) {
		if (stack.isIn(ModItemTags.CANNOT_AUTOMATICALLY_CONSUME)) {
			PlayerEntity player = MinecraftClient.getInstance().player;
			if (player != null && !player.isSpectator() && EnchancementUtil.hasAnyEnchantmentsIn(player, ModEnchantmentTags.AUTOMATICALLY_FEEDS)) {
				lines.add(1, Text.translatable("tooltip." + Enchancement.MOD_ID + ".cannot_auto_consume").formatted(Formatting.RED));
			}
		}
	}
}
