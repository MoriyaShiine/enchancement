/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.event;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BouncyRenderEvent implements HudRenderCallback {
	private static final Identifier ICONS = new Identifier("textures/gui/icons.png");

	@Override
	public void onHudRender(DrawContext drawContext, float tickDelta) {
		ModEntityComponents.BOUNCY.maybeGet(MinecraftClient.getInstance().getCameraEntity()).ifPresent(bouncyComponent -> {
			if (bouncyComponent.hasBouncy()) {
				float boostProgress = bouncyComponent.getBoostProgress();
				if (boostProgress > 0) {
					int width = drawContext.getScaledWindowWidth() / 2 - 91, height = drawContext.getScaledWindowHeight() - 32 + 3;
					drawContext.drawTexture(ICONS, width, height, 0, 84, 182, 5, 256, 256);
					drawContext.drawTexture(ICONS, width, height, 0, 89, (int) (182 * boostProgress), 5, 256, 256);
				}
			}
		});
	}
}
