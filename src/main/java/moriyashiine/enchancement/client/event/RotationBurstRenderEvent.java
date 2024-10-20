/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class RotationBurstRenderEvent implements HudRenderCallback {
	private static final Identifier BACKGROUND_TEXTURE = Enchancement.id("hud/burst_background");
	private static final Identifier PROGRESS_TEXTURE = Enchancement.id("hud/burst_progress");

	@Override
	public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
		ModEntityComponents.ROTATION_BURST.maybeGet(MinecraftClient.getInstance().cameraEntity).ifPresent(rotationBurstComponent -> {
			if (rotationBurstComponent.hasRotationBurst() && rotationBurstComponent.getCooldown() > 0) {
				RenderSystem.enableBlend();
				int x = drawContext.getScaledWindowWidth() / 2 - 5, y = drawContext.getScaledWindowHeight() / 2 + 18;
				drawContext.drawGuiTexture(BACKGROUND_TEXTURE, x, y, 10, 4);
				if (rotationBurstComponent.getCooldown() < rotationBurstComponent.getLastCooldown()) {
					drawContext.drawGuiTexture(PROGRESS_TEXTURE, 10, 4, 0, 0, x, y, (int) (11 - (rotationBurstComponent.getCooldown() / (float) rotationBurstComponent.getLastCooldown()) * 10), 4);
				}
				RenderSystem.disableBlend();
			}
		});
	}
}
