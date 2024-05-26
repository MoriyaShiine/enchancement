/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.freeenchantedbookmerging;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
	@Shadow
	@Final
	private Property levelCost;

	public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(type, syncId, playerInventory, context);
	}

	@Inject(method = "canTakeOutput", at = @At("HEAD"), cancellable = true)
	private void enchancement$freeEnchantedBookMerging(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> cir) {
		if (ModConfig.freeEnchantedBookMerging && input.getStack(1).isOf(Items.ENCHANTED_BOOK)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "updateResult", at = @At("TAIL"))
	private void enchancement$freeEnchantedBookMerging(CallbackInfo ci) {
		if (ModConfig.freeEnchantedBookMerging && input.getStack(1).isOf(Items.ENCHANTED_BOOK)) {
			levelCost.set(0);
		}
	}
}
