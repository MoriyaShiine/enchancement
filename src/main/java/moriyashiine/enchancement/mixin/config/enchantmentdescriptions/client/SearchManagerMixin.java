/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.enchantmentdescriptions.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.event.config.EnchantmentDescriptionsEvent;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.search.SearchManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(SearchManager.class)
public class SearchManagerMixin {
	@ModifyExpressionValue(method = "method_60365", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getTooltip(Lnet/minecraft/item/Item$TooltipContext;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/tooltip/TooltipType;)Ljava/util/List;"))
	private static List<Text> enchancement$enchantmentDescriptions(List<Text> original, @Local(argsOnly = true) ItemStack stack) {
		if (EnchantmentDescriptionsEvent.enableDescriptions() && stack.contains(DataComponentTypes.STORED_ENCHANTMENTS)) {
			original = new ArrayList<>(original);
			ItemEnchantmentsComponent component = stack.get(DataComponentTypes.STORED_ENCHANTMENTS);
			for (RegistryEntry<Enchantment> entry : component.getEnchantments()) {
				original.add(Text.translatable(EnchancementUtil.getTranslationKey(entry)));
			}
		}
		return original;
	}
}
