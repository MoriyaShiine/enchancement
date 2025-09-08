/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.hud;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.AirJumpComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class AirJumpHudElement implements HudElement {
	private static final Identifier[] TEXTURES = new Identifier[16];

	static {
		for (int i = 0; i < TEXTURES.length; i++) {
			TEXTURES[i] = Enchancement.id("hud/air_jump_" + i);
		}
	}

	@Override
	public void render(DrawContext context, RenderTickCounter tickCounter) {
		PlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null && !player.isSpectator()) {
			AirJumpComponent airJumpComponent = ModEntityComponents.AIR_JUMP.get(player);
			if (airJumpComponent.hasAirJump()) {
				int jumpsLeft = airJumpComponent.getJumpsLeft();
				if (jumpsLeft < airJumpComponent.getMaxJumps()) {
					Identifier first = getTexture(jumpsLeft + 1);
					Identifier second = getTexture(jumpsLeft);
					int x = context.getScaledWindowWidth() / 2 - 5, y = context.getScaledWindowHeight() / 2 + 27;
					if (airJumpComponent.getCooldown() < airJumpComponent.getLastCooldown()) {
						context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, first, x, y, 9, 9);
						context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, second, 9, 9, 0, 0, x, y, 9, (int) ((airJumpComponent.getCooldown() / (float) airJumpComponent.getLastCooldown()) * 9));
					} else {
						context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, second, x, y, 9, 9);
					}
				}
			}
		}
	}

	private static Identifier getTexture(int i) {
		i %= TEXTURES.length;
		if (i < 0) {
			i += TEXTURES.length;
		}
		return TEXTURES[i];
	}
}
