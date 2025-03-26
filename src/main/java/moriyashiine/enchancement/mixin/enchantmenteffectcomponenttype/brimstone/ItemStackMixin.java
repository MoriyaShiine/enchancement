/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.brimstone;

import moriyashiine.enchancement.common.init.ModComponentTypes;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder {
	@Inject(method = "appendComponentTooltip", at = @At("HEAD"), cancellable = true)
	private <T extends TooltipAppender> void enchancement$brimstone(ComponentType<T> componentType, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type, CallbackInfo ci) {
		if (componentType == DataComponentTypes.CHARGED_PROJECTILES && contains(ModComponentTypes.BRIMSTONE_DAMAGE)) {
			MutableText hearts = Texts.bracketed(Text.literal("❤").append(" x" + get(ModComponentTypes.BRIMSTONE_DAMAGE) / 2).formatted(Formatting.RED)).formatted(Formatting.DARK_RED);
			textConsumer.accept(Text.translatable("item.minecraft.crossbow.projectile.single", Text.translatable("enchantment.enchancement.brimstone").append(" ").append(hearts)));
			ci.cancel();
		}
	}
}
