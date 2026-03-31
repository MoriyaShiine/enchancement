/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.brimstone;

import moriyashiine.enchancement.common.init.ModComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
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
public abstract class ItemStackMixin implements DataComponentHolder {
	@Inject(method = "addToTooltip", at = @At("HEAD"), cancellable = true)
	private <T extends TooltipProvider> void enchancement$brimstone(DataComponentType<T> type, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag, CallbackInfo ci) {
		if (type == DataComponents.CHARGED_PROJECTILES && has(ModComponentTypes.BRIMSTONE_DAMAGE)) {
			MutableComponent hearts = ComponentUtils.wrapInSquareBrackets(Component.literal("❤").append(" x" + get(ModComponentTypes.BRIMSTONE_DAMAGE) / 2).withStyle(ChatFormatting.RED)).withStyle(ChatFormatting.DARK_RED);
			consumer.accept(Component.translatable("item.minecraft.crossbow.projectile.single", Component.translatable("enchantment.enchancement.brimstone").append(" ").append(hearts)));
			ci.cancel();
		}
	}
}
