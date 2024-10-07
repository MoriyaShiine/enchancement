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

public class DirectionMovementBurstRenderEvent implements HudRenderCallback {
	private static final Identifier BACKGROUND_TEXTURE = Enchancement.id("hud/rotation_movement_burst_background");
	private static final Identifier PROGRESS_TEXTURE = Enchancement.id("hud/rotation_movement_burst_progress");

	@Override
	public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
		ModEntityComponents.DIRECTION_MOVEMENT_BURST.maybeGet(MinecraftClient.getInstance().cameraEntity).ifPresent(directionMovementBurstComponent -> {
			if (directionMovementBurstComponent.hasDirectionMovementBurst() && directionMovementBurstComponent.getCooldown() > 0) {
				RenderSystem.enableBlend();
				int x = drawContext.getScaledWindowWidth() / 2 - 5, y = drawContext.getScaledWindowHeight() / 2 - 14;
				drawContext.drawGuiTexture(BACKGROUND_TEXTURE, x, y, 10, 4);
				if (directionMovementBurstComponent.getCooldown() < directionMovementBurstComponent.getLastCooldown()) {
					drawContext.drawGuiTexture(PROGRESS_TEXTURE, 10, 4, 0, 0, x, y, (int) (11 - (directionMovementBurstComponent.getCooldown() / (float) directionMovementBurstComponent.getLastCooldown()) * 10), 4);
				}
				RenderSystem.disableBlend();
			}
		});
	}
}
