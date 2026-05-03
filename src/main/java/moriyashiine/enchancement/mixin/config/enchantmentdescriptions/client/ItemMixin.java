/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.enchantmentdescriptions.client;

import moriyashiine.enchancement.client.event.config.EnchantmentDescriptionsClientEvent;
import moriyashiine.enchancement.client.gui.screens.inventory.tooltip.StoredEnchantmentsTooltipComponent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Item.class)
public class ItemMixin {
	@Inject(method = "getTooltipImage", at = @At("HEAD"), cancellable = true)
	private void enchancement$enchantmentDescriptions(ItemStack itemStack, CallbackInfoReturnable<Optional<TooltipComponent>> cir) {
		if (EnchantmentDescriptionsClientEvent.enableDescriptions()) {
			ItemEnchantments enchantments = itemStack.get(DataComponents.STORED_ENCHANTMENTS);
			if (enchantments != null) {
				cir.setReturnValue(Optional.of(new StoredEnchantmentsTooltipComponent(enchantments)));
			}
		}
	}
}
