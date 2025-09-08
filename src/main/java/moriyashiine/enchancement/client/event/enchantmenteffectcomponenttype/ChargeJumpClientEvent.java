/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.ChargeJumpComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.strawberrylib.api.event.client.DisableHudBarEvent;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.player.PlayerEntity;

public class ChargeJumpClientEvent implements DisableHudBarEvent {
	@Override
	public TriState shouldDisableHudBar(PlayerEntity player) {
		ChargeJumpComponent chargeJumpComponent = ModEntityComponents.CHARGE_JUMP.get(player);
		if (chargeJumpComponent.hasChargeJump() && chargeJumpComponent.getChargeProgress() > 0) {
			return TriState.TRUE;
		}
		return TriState.DEFAULT;
	}
}
