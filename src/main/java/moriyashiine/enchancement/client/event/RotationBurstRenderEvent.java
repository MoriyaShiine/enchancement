/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.enchancement.api.event.client.HudAddition;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class RotationBurstRenderEvent implements HudAddition {
	private static final Identifier BACKGROUND_TEXTURE = Enchancement.id("hud/burst_background");
	private static final Identifier PROGRESS_TEXTURE = Enchancement.id("hud/burst_progress");

	@Override
	public void render(DrawContext drawContext, RenderTickCounter tickCounter) {
		ModEntityComponents.ROTATION_BURST.maybeGet(MinecraftClient.getInstance().getCameraEntity()).ifPresent(rotationBurstComponent -> {
			if (rotationBurstComponent.hasRotationBurst() && rotationBurstComponent.getCooldown() > 0) {
				RenderSystem.enableBlend();
				int x = drawContext.getScaledWindowWidth() / 2 - 5, y = drawContext.getScaledWindowHeight() / 2 + 18;
				drawContext.drawGuiTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, x, y, 10, 4);
				if (rotationBurstComponent.getCooldown() < rotationBurstComponent.getLastCooldown()) {
					drawContext.drawGuiTexture(RenderLayer::getGuiTextured, PROGRESS_TEXTURE, 10, 4, 0, 0, x, y, (int) (11 - (rotationBurstComponent.getCooldown() / (float) rotationBurstComponent.getLastCooldown()) * 10), 4);
				}
				RenderSystem.disableBlend();
			}
		});
	}
}
