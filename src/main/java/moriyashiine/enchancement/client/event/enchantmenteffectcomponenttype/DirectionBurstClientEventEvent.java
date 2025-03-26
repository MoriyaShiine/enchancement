/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LayeredDrawerWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class DirectionBurstClientEventEvent implements HudLayerRegistrationCallback {
	private static final Identifier BACKGROUND_TEXTURE = Enchancement.id("hud/burst_background");
	private static final Identifier PROGRESS_TEXTURE = Enchancement.id("hud/burst_progress");

	@Override
	public void register(LayeredDrawerWrapper layeredDrawer) {
		layeredDrawer.attachLayerAfter(IdentifiedLayer.CROSSHAIR, Enchancement.id("direction_burst"), (context, tickCounter) -> ModEntityComponents.DIRECTION_BURST.maybeGet(MinecraftClient.getInstance().getCameraEntity()).ifPresent(directionBurstComponent -> {
			if (directionBurstComponent.hasDirectionBurst() && directionBurstComponent.getCooldown() > 0) {
				int x = context.getScaledWindowWidth() / 2 - 5, y = context.getScaledWindowHeight() / 2 - 14;
				context.drawGuiTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, x, y, 10, 4);
				if (directionBurstComponent.getCooldown() < directionBurstComponent.getLastCooldown()) {
					context.drawGuiTexture(RenderLayer::getGuiTextured, PROGRESS_TEXTURE, 10, 4, 0, 0, x, y, (int) (11 - (directionBurstComponent.getCooldown() / (float) directionBurstComponent.getLastCooldown()) * 10), 4);
				}
			}
		}));
	}
}
