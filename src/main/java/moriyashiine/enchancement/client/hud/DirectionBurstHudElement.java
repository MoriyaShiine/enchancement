/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.hud;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.DirectionBurstComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class DirectionBurstHudElement implements HudElement {
	private static final Identifier BACKGROUND_TEXTURE = Enchancement.id("hud/burst_background");
	private static final Identifier PROGRESS_TEXTURE = Enchancement.id("hud/burst_progress");

	@Override
	public void render(DrawContext context, RenderTickCounter tickCounter) {
		PlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null && !player.isSpectator()) {
			DirectionBurstComponent directionBurstComponent = ModEntityComponents.DIRECTION_BURST.get(player);
			if (directionBurstComponent.hasDirectionBurst() && directionBurstComponent.getCooldown() > 0) {
				int x = context.getScaledWindowWidth() / 2 - 5, y = context.getScaledWindowHeight() / 2 - 14;
				context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, x, y, 10, 4);
				if (directionBurstComponent.getCooldown() < directionBurstComponent.getLastCooldown()) {
					context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, PROGRESS_TEXTURE, 10, 4, 0, 0, x, y, (int) (11 - (directionBurstComponent.getCooldown() / (float) directionBurstComponent.getLastCooldown()) * 10), 4);
				}
			}
		}
	}
}
