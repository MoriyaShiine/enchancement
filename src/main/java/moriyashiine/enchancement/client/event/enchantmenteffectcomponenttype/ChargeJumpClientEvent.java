/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.ChargeJumpComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.strawberrylib.api.event.client.DisableHudBarEvent;
import net.minecraft.entity.player.PlayerEntity;

public class ChargeJumpClientEvent implements DisableHudBarEvent {
	@Override
	public boolean shouldDisableHudBar(PlayerEntity player) {
		if (player == null) { // todo slib will have this check built in next version, so remove it then
			return false;
		}
		ChargeJumpComponent chargeJumpComponent = ModEntityComponents.CHARGE_JUMP.get(player);
		return chargeJumpComponent.hasChargeJump() && chargeJumpComponent.getChargeProgress() > 0;
	}
}
