/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.enchantmentdescriptions.client;

import moriyashiine.enchancement.client.event.config.EnchantmentDescriptionsEvent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Inject(method = "addToTooltip", at = @At("HEAD"), cancellable = true)
	private <T extends TooltipProvider> void enchancement$enchantmentDescriptions(DataComponentType<T> type, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag, CallbackInfo ci) {
		if (type == DataComponents.STORED_ENCHANTMENTS && EnchantmentDescriptionsEvent.enableDescriptions()) {
			ci.cancel();
		}
	}
}
