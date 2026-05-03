/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.enchantmentdescriptions.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.event.config.EnchantmentDescriptionsClientEvent;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.multiplayer.SessionSearchTrees;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(SessionSearchTrees.class)
public class SessionSearchTreesMixin {
	@ModifyExpressionValue(method = "lambda$getTooltipLines$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getTooltipLines(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;"))
	private static List<Component> enchancement$enchantmentDescriptions(List<Component> original, @Local(argsOnly = true) ItemStack item) {
		if (EnchantmentDescriptionsClientEvent.enableDescriptions() && item.has(DataComponents.STORED_ENCHANTMENTS)) {
			original = new ArrayList<>(original);
			ItemEnchantments component = item.get(DataComponents.STORED_ENCHANTMENTS);
			for (Holder<Enchantment> enchantment : component.keySet()) {
				original.add(Component.translatable(EnchancementUtil.getTranslationKey(enchantment)));
			}
		}
		return original;
	}
}
