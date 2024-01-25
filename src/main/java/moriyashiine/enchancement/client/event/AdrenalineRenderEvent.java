/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.event;

import moriyashiine.enchancement.common.event.AdrenalineEvent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;

import java.util.List;

public class AdrenalineRenderEvent implements ItemTooltipCallback {
	@Override
	public void getTooltip(ItemStack stack, TooltipContext context, List<Text> lines) {
		int level = EnchantmentHelper.getLevel(ModEnchantments.ADRENALINE, stack);
		if (level > 0) {
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
							float multiplier = AdrenalineEvent.getMultiplier(MinecraftClient.getInstance().player, level);
							if (multiplier != 1) {
								lines.add(i + 1, Text.translatable("attribute.modifier.plus.2", ItemStack.MODIFIER_FORMAT.format(multiplier * 100 - 100), Text.translatable(EntityAttributes.GENERIC_MOVEMENT_SPEED.getTranslationKey())).formatted(Formatting.BLUE));
							}
							break;
						}
					}
				}
			}
		}
	}
}
