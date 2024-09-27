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

public class DashRenderEvent implements HudRenderCallback {
	private static final Identifier DASH_BACKGROUND_TEXTURE = Enchancement.id("hud/dash_background");
	private static final Identifier DASH_PROGRESS_TEXTURE = Enchancement.id("hud/dash_progress");

	@Override
	public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
		ModEntityComponents.ROTATION_MOVEMENT_BURST.maybeGet(MinecraftClient.getInstance().cameraEntity).ifPresent(dashComponent -> {
			if (dashComponent.hasRotationMovementBurst() && dashComponent.getCooldown() > 0) {
				RenderSystem.enableBlend();
				int x = drawContext.getScaledWindowWidth() / 2 - 5, y = drawContext.getScaledWindowHeight() / 2 + 18;
				drawContext.drawGuiTexture(DASH_BACKGROUND_TEXTURE, x, y, 10, 4);
				if (dashComponent.getCooldown() < dashComponent.getLastCooldown()) {
					drawContext.drawGuiTexture(DASH_PROGRESS_TEXTURE, 10, 4, 0, 0, x, y, (int) (11 - (dashComponent.getCooldown() / (float) dashComponent.getLastCooldown()) * 10), 4);
				}
				RenderSystem.disableBlend();
			}
		});
	}
}
