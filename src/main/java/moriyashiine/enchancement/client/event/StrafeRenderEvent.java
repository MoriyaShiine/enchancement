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

public class StrafeRenderEvent implements HudRenderCallback {
	private static final Identifier DASH_TEXTURE = Enchancement.id("textures/gui/dash.png");

	@Override
	public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
		ModEntityComponents.DIRECTION_MOVEMENT_BURST.maybeGet(MinecraftClient.getInstance().cameraEntity).ifPresent(strafeComponent -> {
			if (strafeComponent.hasDirectionMovementBurst() && strafeComponent.getCooldown() > 0) {
				RenderSystem.enableBlend();
				drawContext.drawTexture(DASH_TEXTURE, (int) (drawContext.getScaledWindowWidth() / 2F) - 5, (int) (drawContext.getScaledWindowHeight() / 2F) - 14, 0, 4, 10, 4, 10, 8);
				if (strafeComponent.getCooldown() < strafeComponent.getLastCooldown()) {
					drawContext.drawTexture(DASH_TEXTURE, (int) (drawContext.getScaledWindowWidth() / 2F) - 5, (int) (drawContext.getScaledWindowHeight() / 2F) - 14, 0, 0, (int) (11 - (strafeComponent.getCooldown() / (float) strafeComponent.getLastCooldown()) * 10), 4, 10, 8);
				}
				RenderSystem.disableBlend();
			}
		});
	}
}
