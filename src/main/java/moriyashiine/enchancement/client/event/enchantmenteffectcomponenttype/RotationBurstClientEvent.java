/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LayeredDrawerWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class RotationBurstClientEvent implements HudLayerRegistrationCallback {
	private static final Identifier BACKGROUND_TEXTURE = Enchancement.id("hud/burst_background");
	private static final Identifier PROGRESS_TEXTURE = Enchancement.id("hud/burst_progress");

	@Override
	public void register(LayeredDrawerWrapper layeredDrawer) {
		layeredDrawer.attachLayerAfter(IdentifiedLayer.CROSSHAIR, Enchancement.id("rotation_burst"), (context, tickCounter) -> ModEntityComponents.ROTATION_BURST.maybeGet(MinecraftClient.getInstance().getCameraEntity()).ifPresent(rotationBurstComponent -> {
			if (rotationBurstComponent.hasRotationBurst() && rotationBurstComponent.getCooldown() > 0) {
				RenderSystem.enableBlend();
				int x = context.getScaledWindowWidth() / 2 - 5, y = context.getScaledWindowHeight() / 2 + 18;
				context.drawGuiTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, x, y, 10, 4);
				if (rotationBurstComponent.getCooldown() < rotationBurstComponent.getLastCooldown()) {
					context.drawGuiTexture(RenderLayer::getGuiTextured, PROGRESS_TEXTURE, 10, 4, 0, 0, x, y, (int) (11 - (rotationBurstComponent.getCooldown() / (float) rotationBurstComponent.getLastCooldown()) * 10), 4);
				}
				RenderSystem.disableBlend();
			}
		}));
	}
}
