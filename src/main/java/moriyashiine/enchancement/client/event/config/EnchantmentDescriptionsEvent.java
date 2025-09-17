/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event.config;

import moriyashiine.enchancement.client.gui.tooltip.StoredEnchantmentsTooltipComponent;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnchantmentDescriptionsEvent {
	public static class DescriptionText implements ItemTooltipCallback {
		@Override
		public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> lines) {
			if (enableDescriptions()) {
				EnchantmentHelper.getEnchantments(stack).getEnchantments().forEach(enchantment -> {
					for (int i = 0; i < lines.size(); i++) {
						Text description = getDescription(lines.get(i), enchantment, true);
						if (description != null) {
							lines.add(i + 1, description);
							break;
						}
					}
				});
			}
		}
	}

	public static class Icons implements TooltipComponentCallback {
		@Override
		public @Nullable TooltipComponent getComponent(TooltipData data) {
			if (data instanceof StoredEnchantmentsTooltipComponent enchantmentsComponent) {
				return enchantmentsComponent;
			}
			return null;
		}
	}

	public static class ClearIconCache implements CommonLifecycleEvents.TagsLoaded {
		@Override
		public void onTagsLoaded(DynamicRegistryManager registries, boolean client) {
			if (client) {
				StoredEnchantmentsTooltipComponent.clearIconCache();
			}
		}
	}

	public static boolean enableDescriptions() {
		return ModConfig.enchantmentDescriptions && !Enchancement.commonEnchantmentDescriptionsModLoaded;
	}

	@Nullable
	public static Text getDescription(Text text, RegistryEntry<Enchantment> enchantment, boolean hyphen) {
		String translationKey = EnchancementUtil.getTranslationKey(enchantment);
		if (text.getContent() instanceof TranslatableTextContent textContent && textContent.getKey().equals(translationKey)) {
			MutableText description = Text.translatable(translationKey + ".desc").formatted(Formatting.DARK_GRAY);
			if (!description.getString().isEmpty()) {
				return hyphen ? Text.literal(" - ").formatted(Formatting.GRAY).append(description) : description;
			}
		}
		return null;
	}
}
