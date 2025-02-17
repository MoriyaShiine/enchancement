/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LayeredDrawerWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class AirJumpRenderEvent implements HudLayerRegistrationCallback {
	private static final Identifier[] TEXTURES = new Identifier[16];

	static {
		for (int i = 0; i < TEXTURES.length; i++) {
			TEXTURES[i] = Enchancement.id("hud/air_jump_" + i);
		}
	}

	@Override
	public void register(LayeredDrawerWrapper layeredDrawer) {
		layeredDrawer.attachLayerAfter(IdentifiedLayer.CROSSHAIR, Enchancement.id("air_jump"), (context, tickCounter) -> ModEntityComponents.AIR_JUMP.maybeGet(MinecraftClient.getInstance().getCameraEntity()).ifPresent(airJumpComponent -> {
			if (airJumpComponent.hasAirJump()) {
				int jumpsLeft = airJumpComponent.getJumpsLeft();
				if (jumpsLeft < airJumpComponent.getMaxJumps()) {
					RenderSystem.enableBlend();
					Identifier first = getTexture(jumpsLeft + 1);
					Identifier second = getTexture(jumpsLeft);
					int x = context.getScaledWindowWidth() / 2 - 5, y = context.getScaledWindowHeight() / 2 + 27;
					if (airJumpComponent.getCooldown() < airJumpComponent.getLastCooldown()) {
						context.drawGuiTexture(RenderLayer::getGuiTextured, first, x, y, 9, 9);
						context.drawGuiTexture(RenderLayer::getGuiTextured, second, 9, 9, 0, 0, x, y, 9, (int) ((airJumpComponent.getCooldown() / (float) airJumpComponent.getLastCooldown()) * 9));
					} else {
						context.drawGuiTexture(RenderLayer::getGuiTextured, second, x, y, 9, 9);
					}
					RenderSystem.disableBlend();
				}
			}
		}));
	}

	private static Identifier getTexture(int i) {
		i %= TEXTURES.length;
		if (i < 0) {
			i += TEXTURES.length;
		}
		return TEXTURES[i];
	}
}
