/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.overhaulenchanting;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
	@Shadow
	@Final
	private DataSlot cost;

	@Shadow
	private @Nullable String itemName;

	public AnvilMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition itemInputSlots) {
		super(menuType, containerId, inventory, access, itemInputSlots);
	}

	@Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
	private void enchancement$overhaulEnchanting(Player player, boolean hasItem, CallbackInfoReturnable<Boolean> cir) {
		if (ModConfig.overhaulEnchanting != OverhaulMode.DISABLED && inputSlots.getItem(1).is(Items.ENCHANTED_BOOK)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "createResult", at = @At("TAIL"))
	private void enchancement$overhaulEnchanting(CallbackInfo ci) {
		if (ModConfig.overhaulEnchanting != OverhaulMode.DISABLED) {
			if (inputSlots.getItem(1).is(Items.ENCHANTED_BOOK)) {
				if (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED && !inputSlots.getItem(0).is(Items.ENCHANTED_BOOK)) {
					resultSlots.setItem(0, ItemStack.EMPTY);
					broadcastChanges();
					return;
				}
				if (itemName == null || StringUtil.isBlank(itemName) || inputSlots.getItem(0).getHoverName().getString().equals(itemName)) {
					cost.set(0);
				} else {
					cost.set(1);
				}
			}
			ItemStack outputStack = resultSlots.getItem(0);
			if (outputStack.has(DataComponents.REPAIR_COST)) {
				outputStack.set(DataComponents.REPAIR_COST, 0);
			}
			broadcastChanges();
		}
	}
}
