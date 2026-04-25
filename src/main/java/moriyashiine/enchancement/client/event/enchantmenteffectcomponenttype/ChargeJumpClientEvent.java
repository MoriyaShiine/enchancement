/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.ChargeJumpComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.strawberrylib.api.event.client.DisableContextualInfoEvent;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.world.entity.player.Player;

public class ChargeJumpClientEvent implements DisableContextualInfoEvent {
	@Override
	public TriState shouldDisable(Player player) {
		ChargeJumpComponent chargeJumpComponent = ModEntityComponents.CHARGE_JUMP.get(player);
		if (chargeJumpComponent.hasChargeJump() && chargeJumpComponent.getChargeProgress() > 0) {
			return TriState.TRUE;
		}
		return TriState.DEFAULT;
	}
}
