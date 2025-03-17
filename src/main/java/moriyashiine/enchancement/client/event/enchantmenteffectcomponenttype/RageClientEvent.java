/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.enchantment.effect.RageEffect;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;

import java.util.List;

public class RageClientEvent implements ItemTooltipCallback {
	@Override
	public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> lines) {
		float speedMultiplier = 1 + RageEffect.getMovementSpeedModifier(MinecraftClient.getInstance().player, stack);
		float damageMultiplier = RageEffect.getDamageTakenModifier(MinecraftClient.getInstance().player, stack);

		if (speedMultiplier != 1 || damageMultiplier != 1) {
			for (int i = 0; i < lines.size(); i++) {
				if (lines.get(i).getContent() instanceof TranslatableTextContent content) {
					if (content.getKey().startsWith("attribute.modifier")) {
						boolean last = true;
						if (i + 1 < lines.size() && lines.get(i + 1).getContent() instanceof TranslatableTextContent nextContent) {
							if (nextContent.getKey().startsWith("attribute.modifier")) {
								last = false;
							}
						}
						if (last) {
							if (speedMultiplier != 1) {
								lines.add(i + 1, Text.translatable("attribute.modifier.plus.2", AttributeModifiersComponent.DECIMAL_FORMAT.format(speedMultiplier * 100 - 100), Text.translatable(EntityAttributes.MOVEMENT_SPEED.value().getTranslationKey())).formatted(Formatting.BLUE));
							}
							if (damageMultiplier != 1) {
								lines.add(i + 1, Text.translatable("attribute.modifier.plus.2", AttributeModifiersComponent.DECIMAL_FORMAT.format(-(damageMultiplier * 100 - 100)), Text.translatable("tooltip." + Enchancement.MOD_ID + ".damage_resistance")).formatted(Formatting.BLUE));
							}
							break;
						}
					}
				}
			}
		}
	}
}
