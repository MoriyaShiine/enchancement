/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.hud;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.RotationBurstComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class RotationBurstHudElement implements HudElement {
	private static final Identifier BACKGROUND_TEXTURE = Enchancement.id("hud/burst_background");
	private static final Identifier PROGRESS_TEXTURE = Enchancement.id("hud/burst_progress");

	@Override
	public void render(DrawContext context, RenderTickCounter tickCounter) {
		PlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null && !player.isSpectator()) {
			RotationBurstComponent rotationBurstComponent = ModEntityComponents.ROTATION_BURST.get(player);
			if (rotationBurstComponent.hasRotationBurst() && rotationBurstComponent.getCooldown() > 0) {
				int x = context.getScaledWindowWidth() / 2 - 5, y = context.getScaledWindowHeight() / 2 + 18;
				context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, x, y, 10, 4);
				if (rotationBurstComponent.getCooldown() < rotationBurstComponent.getLastCooldown()) {
					context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, PROGRESS_TEXTURE, 10, 4, 0, 0, x, y, (int) (11 - (rotationBurstComponent.getCooldown() / (float) rotationBurstComponent.getLastCooldown()) * 10), 4);
				}
			}
		}
	}
}
