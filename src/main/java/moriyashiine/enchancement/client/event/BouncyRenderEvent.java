/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.event;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class BouncyRenderEvent implements HudRenderCallback {
	private static final Identifier JUMP_BAR_BACKGROUND_TEXTURE = new Identifier("hud/jump_bar_background");
	private static final Identifier JUMP_BAR_PROGRESS_TEXTURE = new Identifier("hud/jump_bar_progress");

	@Override
	public void onHudRender(DrawContext drawContext, float tickDelta) {
		ModEntityComponents.BOUNCY.maybeGet(MinecraftClient.getInstance().getCameraEntity()).ifPresent(bouncyComponent -> {
			if (bouncyComponent.hasBouncy()) {
				float boostProgress = bouncyComponent.getBoostProgress();
				if (boostProgress > 0) {
					int x = drawContext.getScaledWindowWidth() / 2 - 91, y = drawContext.getScaledWindowHeight() - 29;
					drawContext.drawGuiTexture(JUMP_BAR_BACKGROUND_TEXTURE, x, y, 182, 5);
					drawContext.drawGuiTexture(JUMP_BAR_PROGRESS_TEXTURE, 182, 5, 0, 0, x, y, (int) (182 * boostProgress), 5);
				}
			}
		});
	}
}
