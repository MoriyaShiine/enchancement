/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceequipment.client;

import moriyashiine.enchancement.client.event.config.RebalanceEquipmentClientEvent;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Shadow
	@Nullable
	public LocalPlayer player;

	@Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
	private void enchancment$rebalanceEquipment(CallbackInfoReturnable<Boolean> cir) {
		if (ModConfig.rebalanceEquipment && player != null && !ItemStack.matches(RebalanceEquipmentClientEvent.lastStack, player.getMainHandItem())) {
			cir.setReturnValue(false);
		}
	}
}
