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
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class AirJumpRenderEvent implements HudRenderCallback {
	private static final Identifier[] TEXTURES = new Identifier[16];

	static {
		for (int i = 0; i < TEXTURES.length; i++) {
			TEXTURES[i] = Enchancement.id("hud/air_jump_" + i);
		}
	}

	@Override
	public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
		ModEntityComponents.AIR_JUMP.maybeGet(MinecraftClient.getInstance().getCameraEntity()).ifPresent(airJumpComponent -> {
			if (airJumpComponent.hasAirJump()) {
				int jumpsLeft = airJumpComponent.getJumpsLeft();
				if (jumpsLeft < airJumpComponent.getMaxJumps()) {
					RenderSystem.enableBlend();
					Identifier first = getTexture(jumpsLeft + 1);
					Identifier second = getTexture(jumpsLeft);
					int x = drawContext.getScaledWindowWidth() / 2 - 5, y = drawContext.getScaledWindowHeight() / 2 + 27;
					if (airJumpComponent.getCooldown() < airJumpComponent.getLastCooldown()) {
						drawContext.drawGuiTexture(RenderLayer::getGuiTextured, first, x, y, 9, 9);
						drawContext.drawGuiTexture(RenderLayer::getGuiTextured, second, 9, 9, 0, 0, x, y, 9, (int) ((airJumpComponent.getCooldown() / (float) airJumpComponent.getLastCooldown()) * 9));
					} else {
						drawContext.drawGuiTexture(RenderLayer::getGuiTextured, second, x, y, 9, 9);
					}
					RenderSystem.disableBlend();
				}
			}
		});
	}

	private static Identifier getTexture(int i) {
		i %= TEXTURES.length;
		if (i < 0) {
			i += TEXTURES.length;
		}
		return TEXTURES[i];
	}
}
