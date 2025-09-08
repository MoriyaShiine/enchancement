/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.hud;

import moriyashiine.enchancement.common.component.entity.ChargeJumpComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ChargeJumpHudElement implements HudElement {
	private static final Identifier BACKGROUND_TEXTURE = Identifier.of("hud/jump_bar_background");
	private static final Identifier PROGRESS_TEXTURE = Identifier.of("hud/jump_bar_progress");

	@Override
	public void render(DrawContext context, RenderTickCounter tickCounter) {
		PlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null && !player.isSpectator()) {
			ChargeJumpComponent chargeJumpComponent = ModEntityComponents.CHARGE_JUMP.get(player);
			if (chargeJumpComponent.hasChargeJump()) {
				float boostProgress = chargeJumpComponent.getChargeProgress();
				if (boostProgress > 0) {
					int x = context.getScaledWindowWidth() / 2 - 91, y = context.getScaledWindowHeight() - 29;
					context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, x, y, 182, 5);
					context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, PROGRESS_TEXTURE, 182, 5, 0, 0, x, y, (int) (182 * boostProgress), 5);
				}
			}
		}
	}
}
