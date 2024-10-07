/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class ChargeJumpRenderEvent implements HudRenderCallback {
	private static final Identifier BACKGROUND_TEXTURE = Identifier.of("hud/jump_bar_background");
	private static final Identifier PROGRESS_TEXTURE = Identifier.of("hud/jump_bar_progress");

	@Override
	public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
		ModEntityComponents.CHARGE_JUMP.maybeGet(MinecraftClient.getInstance().getCameraEntity()).ifPresent(chargeJumpComponent -> {
			if (chargeJumpComponent.hasChargeJump()) {
				float boostProgress = chargeJumpComponent.getChargeProgress();
				if (boostProgress > 0) {
					int x = drawContext.getScaledWindowWidth() / 2 - 91, y = drawContext.getScaledWindowHeight() - 29;
					drawContext.drawGuiTexture(BACKGROUND_TEXTURE, x, y, 182, 5);
					drawContext.drawGuiTexture(PROGRESS_TEXTURE, 182, 5, 0, 0, x, y, (int) (182 * boostProgress), 5);
				}
			}
		});
	}
}
