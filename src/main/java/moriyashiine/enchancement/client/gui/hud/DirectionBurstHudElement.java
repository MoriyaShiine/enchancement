/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.gui.hud;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.DirectionBurstComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class DirectionBurstHudElement implements HudElement {
	private static final Identifier BACKGROUND_TEXTURE = Enchancement.id("hud/burst_background");
	private static final Identifier PROGRESS_TEXTURE = Enchancement.id("hud/burst_progress");

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
		Player player = Minecraft.getInstance().player;
		if (player != null && !player.isSpectator()) {
			DirectionBurstComponent directionBurstComponent = ModEntityComponents.DIRECTION_BURST.get(player);
			if (directionBurstComponent.hasDirectionBurst() && directionBurstComponent.getCooldown() > 0) {
				int x = graphics.guiWidth() / 2 - 5, y = graphics.guiHeight() / 2 - 14;
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, x, y, 10, 4);
				if (directionBurstComponent.getCooldown() < directionBurstComponent.getLastCooldown()) {
					graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_TEXTURE, 10, 4, 0, 0, x, y, (int) (11 - (directionBurstComponent.getCooldown() / (float) directionBurstComponent.getLastCooldown()) * 10), 4);
				}
			}
		}
	}
}
