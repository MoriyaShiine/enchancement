/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.event.config;

import moriyashiine.enchancement.client.gui.screens.inventory.tooltip.StoredEnchantmentsTooltipComponent;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class EnchantmentDescriptionsEvent {
	public static class DescriptionText implements ItemTooltipCallback {
		@Override
		public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, List<Component> lines) {
			if (enableDescriptions()) {
				EnchantmentHelper.getEnchantmentsForCrafting(stack).keySet().forEach(enchantment -> {
					for (int i = 0; i < lines.size(); i++) {
						List<Component> description = getDescription(lines.get(i), enchantment);
						if (description != null) {
							lines.addAll(i + 1, description);
							break;
						}
					}
				});
			}
		}
	}

	public static class Icons implements ClientTooltipComponentCallback {
		@Override
		public @Nullable ClientTooltipComponent getClientComponent(TooltipComponent component) {
			if (component instanceof StoredEnchantmentsTooltipComponent enchantmentsComponent) {
				return enchantmentsComponent;
			}
			return null;
		}
	}

	public static class ClearIconCache implements CommonLifecycleEvents.TagsLoaded {
		@Override
		public void onTagsLoaded(RegistryAccess registries, boolean client) {
			if (client) {
				StoredEnchantmentsTooltipComponent.clearIconCache();
			}
		}
	}

	public static boolean enableDescriptions() {
		return ModConfig.enchantmentDescriptions && !Enchancement.commonEnchantmentDescriptionsModLoaded;
	}

	@Nullable
	public static List<Component> getDescription(Component component, Holder<Enchantment> enchantment) {
		String translationKey = EnchancementUtil.getTranslationKey(enchantment);
		if (component.getContents() instanceof TranslatableContents translatable && translatable.getKey().equals(translationKey)) {
			MutableComponent description = Component.translatable(translationKey + ".desc").withStyle(ChatFormatting.DARK_GRAY);
			if (!description.getString().isEmpty()) {
				return SLibClientUtils.wrapText(Component.literal(" - ").withStyle(ChatFormatting.GRAY).append(description));
			}
		}
		return null;
	}
}
