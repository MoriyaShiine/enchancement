/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.toggleablepassives;

import moriyashiine.enchancement.common.init.ModComponentTypes;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder {
	@Shadow
	@Nullable
	public abstract <T> T set(DataComponentType<? super T> type, @Nullable T value);

	@Inject(method = "overrideOtherStackedOnMe", at = @At("HEAD"), cancellable = true)
	private void enchancement$toggleablePassives(ItemStack other, Slot slot, ClickAction clickAction, Player player, SlotAccess carriedItem, CallbackInfoReturnable<Boolean> cir) {
		if (clickAction == ClickAction.SECONDARY && has(ModComponentTypes.TOGGLEABLE_PASSIVE)) {
			set(ModComponentTypes.TOGGLEABLE_PASSIVE, !get(ModComponentTypes.TOGGLEABLE_PASSIVE));
			if (player.level().isClientSide()) {
				player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1, 1);
			}
			cir.setReturnValue(true);
		}
	}
}
