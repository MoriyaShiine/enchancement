/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.event;

import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;

import java.util.List;

@Environment(EnvType.CLIENT)
public class EnchantmentDescriptionsEvent implements ItemTooltipCallback {
	@Override
	public void getTooltip(ItemStack stack, TooltipContext context, List<Text> lines) {
		EnchantmentHelper.get(stack).forEach((enchantment, integer) -> {
			for (int i = 0; i < lines.size(); i++) {
				if (lines.get(i).getContent() instanceof TranslatableTextContent text && text.getKey().equals(enchantment.getTranslationKey())) {
					if (ModConfig.coloredEnchantmentNames && !enchantment.isCursed()) {
						lines.set(i, Text.translatable(text.getKey()).formatted(Formatting.GREEN));
					}
					if (enableDescriptions()) {
						lines.add(i + 1, Text.literal(" - ").formatted(Formatting.GRAY).append(getDescription(enchantment).formatted(Formatting.DARK_GRAY)));
					}
					break;
				}
			}
		});
	}

	private static MutableText getDescription(Enchantment enchantment) {
		if (enchantment == ModEnchantments.STRAFE) {
			return Text.translatable(enchantment.getTranslationKey() + ".desc", Text.translatable((EnchancementClient.STRAFE_KEYBINDING.isUnbound() ? MinecraftClient.getInstance().options.sprintKey : EnchancementClient.STRAFE_KEYBINDING).getTranslationKey()).formatted(Formatting.GOLD));
		}
		return Text.translatable(enchantment.getTranslationKey() + ".desc");
	}

	private static boolean enableDescriptions() {
		return ModConfig.enchantmentDescriptions && !Enchancement.commonEnchantmentDescriptionsModLoaded;
	}
}
