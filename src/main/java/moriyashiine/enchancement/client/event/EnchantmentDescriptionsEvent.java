/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.event;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.ModConfig;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;

import java.util.List;

public class EnchantmentDescriptionsEvent implements ItemTooltipCallback {
	@Override
	public void getTooltip(ItemStack stack, TooltipContext context, List<Text> lines) {
		if (enableDescriptions()) {
			EnchantmentHelper.get(stack).forEach((enchantment, integer) -> {
				for (int i = 0; i < lines.size(); i++) {
					if (lines.get(i).getContent() instanceof TranslatableTextContent text && text.getKey().equals(enchantment.getTranslationKey())) {
						lines.add(i + 1, Text.literal(" - ").formatted(Formatting.GRAY).append(Text.translatable(enchantment.getTranslationKey() + ".desc").formatted(Formatting.DARK_GRAY)));
						break;
					}
				}
			});
		}
	}

	private static boolean enableDescriptions() {
		return ModConfig.enchantmentDescriptions && !Enchancement.commonEnchantmentDescriptionsModLoaded;
	}
}
