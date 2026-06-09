/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class InventoryMixin {
	@Shadow
	@Final
	public Player player;

	@Inject(method = "setSelectedSlot", at = @At("TAIL"))
	private void enchancement$rebalanceEquipment(int selected, CallbackInfo ci) {
		if (ModConfig.rebalanceEquipment) {
			if (!player.level().isClientSide()) {
				player.detectEquipmentUpdates();
			}
			player.resetAttackStrengthTicker();
		}
	}
}
