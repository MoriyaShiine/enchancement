/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.overhaulenchanting;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.util.StringHelper;
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

	@Shadow
	private @Nullable String newItemName;

	public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, ForgingSlotsManager forgingSlotsManager) {
		super(type, syncId, playerInventory, context, forgingSlotsManager);
	}

	@Inject(method = "canTakeOutput", at = @At("HEAD"), cancellable = true)
	private void enchancement$overhaulEnchanting(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> cir) {
		if (ModConfig.overhaulEnchanting != OverhaulMode.DISABLED && input.getStack(1).isOf(Items.ENCHANTED_BOOK)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "updateResult", at = @At("TAIL"))
	private void enchancement$overhaulEnchanting(CallbackInfo ci) {
		if (ModConfig.overhaulEnchanting != OverhaulMode.DISABLED) {
			if (input.getStack(1).isOf(Items.ENCHANTED_BOOK)) {
				if (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED && !input.getStack(0).isOf(Items.ENCHANTED_BOOK)) {
					output.setStack(0, ItemStack.EMPTY);
					sendContentUpdates();
					return;
				}
				if (newItemName == null || StringHelper.isBlank(newItemName) || input.getStack(0).getName().getString().equals(newItemName)) {
					levelCost.set(0);
				} else {
					levelCost.set(1);
				}
			}
			ItemStack outputStack = output.getStack(0);
			if (outputStack.contains(DataComponentTypes.REPAIR_COST)) {
				outputStack.set(DataComponentTypes.REPAIR_COST, 0);
			}
			sendContentUpdates();
		}
	}
}
