/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.event.enchantmenteffecttype;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.tag.ModEnchantmentTags;
import moriyashiine.enchancement.common.tag.ModItemTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class AutomaticallyFeedsTooltipClientEvent implements ItemTooltipCallback {
	@Override
	public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, List<Component> lines) {
		if (stack.is(ModItemTags.CANNOT_AUTOMATICALLY_CONSUME)) {
			Player player = Minecraft.getInstance().player;
			if (player != null && !player.isSpectator() && EnchancementUtil.hasAnyEnchantmentsIn(player, ModEnchantmentTags.AUTOMATICALLY_FEEDS)) {
				lines.add(1, Component.translatable("tooltip." + Enchancement.MOD_ID + ".cannot_auto_consume").withStyle(ChatFormatting.RED));
			}
		}
	}
}
