/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.overhaulenchantingtable.client;

import moriyashiine.enchancement.client.event.config.EnchantmentDescriptionsEvent;
import moriyashiine.enchancement.client.gui.tooltip.StoredEnchantmentsTooltipComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Item.class)
public class ItemMixin {
	@Inject(method = "getTooltipData", at = @At("HEAD"), cancellable = true)
	private void enchancement$overhaulEnchantingTable(ItemStack stack, CallbackInfoReturnable<Optional<TooltipData>> cir) {
		if (EnchantmentDescriptionsEvent.enableDescriptions()) {
			ItemEnchantmentsComponent enchantments = stack.get(DataComponentTypes.STORED_ENCHANTMENTS);
			if (enchantments != null) {
				cir.setReturnValue(Optional.of(new StoredEnchantmentsTooltipComponent(enchantments)));
			}
		}
	}
}
