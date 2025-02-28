/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.overhaulenchantingtable;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.OverhaulMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.ForgingSlotsManager;
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

	public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, ForgingSlotsManager forgingSlotsManager) {
		super(type, syncId, playerInventory, context, forgingSlotsManager);
	}

	@Inject(method = "canTakeOutput", at = @At("HEAD"), cancellable = true)
	private void enchancement$overhaulEnchantingTable(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> cir) {
		if (ModConfig.overhaulEnchantingTable != OverhaulMode.DISABLED && input.getStack(1).isOf(Items.ENCHANTED_BOOK)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "updateResult", at = @At("TAIL"))
	private void enchancement$overhaulEnchantingTable(CallbackInfo ci) {
		if (ModConfig.overhaulEnchantingTable != OverhaulMode.DISABLED && input.getStack(1).isOf(Items.ENCHANTED_BOOK)) {
			levelCost.set(0);
		}
	}
}
