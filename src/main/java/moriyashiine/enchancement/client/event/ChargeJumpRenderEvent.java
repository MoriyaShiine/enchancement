/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LayeredDrawerWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class ChargeJumpRenderEvent implements HudLayerRegistrationCallback {
	private static final Identifier BACKGROUND_TEXTURE = Identifier.of("hud/jump_bar_background");
	private static final Identifier PROGRESS_TEXTURE = Identifier.of("hud/jump_bar_progress");

	@Override
	public void register(LayeredDrawerWrapper layeredDrawer) {
		layeredDrawer.attachLayerAfter(IdentifiedLayer.CROSSHAIR, Enchancement.id("charge_jump"), (context, tickCounter) -> ModEntityComponents.CHARGE_JUMP.maybeGet(MinecraftClient.getInstance().getCameraEntity()).ifPresent(chargeJumpComponent -> {
			if (chargeJumpComponent.hasChargeJump()) {
				float boostProgress = chargeJumpComponent.getChargeProgress();
				if (boostProgress > 0) {
					int x = context.getScaledWindowWidth() / 2 - 91, y = context.getScaledWindowHeight() - 29;
					context.drawGuiTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, x, y, 182, 5);
					context.drawGuiTexture(RenderLayer::getGuiTextured, PROGRESS_TEXTURE, 182, 5, 0, 0, x, y, (int) (182 * boostProgress), 5);
				}
			}
		}));
	}
}
