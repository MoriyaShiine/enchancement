/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.ChargeJumpComponent;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import moriyashiine.strawberrylib.api.event.client.ReplaceContextualInfoEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class ChargeJumpClientEvent implements ReplaceContextualInfoEvent {
	public static void init() {
		ReplaceContextualInfoEvent.EVENT.register(new ChargeJumpClientEvent());
	}

	private static final Identifier BACKGROUND_TEXTURE = Identifier.parse("hud/jump_bar_background");
	private static final Identifier PROGRESS_TEXTURE = Identifier.parse("hud/jump_bar_progress");

	@Override
	public ContextualInfo getInfo(Player player) {
		ChargeJumpComponent chargeJump = EnchancementEntityComponents.CHARGE_JUMP.get(player);
		if (chargeJump.shouldRender()) {
			return new ContextualInfo(BACKGROUND_TEXTURE, PROGRESS_TEXTURE, chargeJump.getChargeProgress());
		}
		return null;
	}
}
