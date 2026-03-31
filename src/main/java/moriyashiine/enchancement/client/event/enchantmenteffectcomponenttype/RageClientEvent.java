/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.world.item.effects.RageEffect;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class RageClientEvent implements ItemTooltipCallback {
	@Override
	public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, List<Component> lines) {
		Player player = Minecraft.getInstance().player;
		float speedMultiplier = 1 + RageEffect.getMovementSpeedModifier(player, stack);
		float damageMultiplier = RageEffect.getDamageTakenModifier(player, stack);

		if (speedMultiplier != 1 || damageMultiplier != 1) {
			for (int i = 0; i < lines.size(); i++) {
				if (lines.get(i).getContents() instanceof TranslatableContents translatable) {
					if (translatable.getKey().startsWith("attribute.modifier")) {
						boolean last = true;
						if (i + 1 < lines.size() && lines.get(i + 1).getContents() instanceof TranslatableContents next) {
							if (next.getKey().startsWith("attribute.modifier")) {
								last = false;
							}
						}
						if (last) {
							if (speedMultiplier != 1) {
								lines.add(i + 1, Component.translatable("attribute.modifier.plus.2", ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(speedMultiplier * 100 - 100), Component.translatable(Attributes.MOVEMENT_SPEED.value().getDescriptionId())).withStyle(ChatFormatting.BLUE));
							}
							if (damageMultiplier != 1) {
								lines.add(i + 1, Component.translatable("attribute.modifier.plus.2", ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(-(damageMultiplier * 100 - 100)), Component.translatable("tooltip." + Enchancement.MOD_ID + ".damage_resistance")).withStyle(ChatFormatting.BLUE));
							}
							break;
						}
					}
				}
			}
		}
	}
}
