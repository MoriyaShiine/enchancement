/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.gui.hud;

import moriyashiine.enchancement.common.component.entity.ChargeJumpComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class ChargeJumpHudElement implements HudElement {
	private static final Identifier BACKGROUND_TEXTURE = Identifier.parse("hud/jump_bar_background");
	private static final Identifier PROGRESS_TEXTURE = Identifier.parse("hud/jump_bar_progress");

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
		Player player = Minecraft.getInstance().player;
		if (player != null && !player.isSpectator()) {
			ChargeJumpComponent chargeJumpComponent = ModEntityComponents.CHARGE_JUMP.get(player);
			if (chargeJumpComponent.hasChargeJump()) {
				float boostProgress = chargeJumpComponent.getChargeProgress();
				if (boostProgress > 0) {
					int x = graphics.guiWidth() / 2 - 91, y = graphics.guiHeight() - 29;
					graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, x, y, 182, 5);
					graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_TEXTURE, 182, 5, 0, 0, x, y, (int) (182 * boostProgress), 5);
				}
			}
		}
	}
}
