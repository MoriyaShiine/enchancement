/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.util.toggleablepassive;

import moriyashiine.enchancement.common.init.ModDataComponentTypes;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.DataComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder {
	@Shadow
	@Nullable
	public abstract <T> T set(DataComponentType<? super T> type, @Nullable T value);

	@Inject(method = "onClicked", at = @At("HEAD"), cancellable = true)
	private void enchancement$toggleablePassive(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> cir) {
		if (clickType == ClickType.RIGHT && contains(ModDataComponentTypes.TOGGLEABLE_PASSIVE)) {
			set(ModDataComponentTypes.TOGGLEABLE_PASSIVE, !get(ModDataComponentTypes.TOGGLEABLE_PASSIVE));
			player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1, 1);
			cir.setReturnValue(true);
		}
	}
}
