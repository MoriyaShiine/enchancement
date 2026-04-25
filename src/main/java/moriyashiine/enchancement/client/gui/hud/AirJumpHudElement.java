/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.gui.hud;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.AirJumpComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class AirJumpHudElement implements HudElement {
	private static final Identifier[] TEXTURES = new Identifier[16];

	static {
		for (int i = 0; i < TEXTURES.length; i++) {
			TEXTURES[i] = Enchancement.id("hud/air_jump_" + i);
		}
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
		Player player = Minecraft.getInstance().player;
		if (player != null && !player.isSpectator()) {
			AirJumpComponent airJumpComponent = ModEntityComponents.AIR_JUMP.get(player);
			if (airJumpComponent.hasAirJump()) {
				int jumpsLeft = airJumpComponent.getJumpsLeft();
				if (jumpsLeft < airJumpComponent.getMaxJumps()) {
					Identifier first = getTexture(jumpsLeft + 1);
					Identifier second = getTexture(jumpsLeft);
					int x = graphics.guiWidth() / 2 - 5, y = graphics.guiHeight() / 2 + 27;
					if (airJumpComponent.getCooldown() < airJumpComponent.getLastCooldown()) {
						graphics.blitSprite(RenderPipelines.GUI_TEXTURED, first, x, y, 9, 9);
						graphics.blitSprite(RenderPipelines.GUI_TEXTURED, second, 9, 9, 0, 0, x, y, 9, (int) ((airJumpComponent.getCooldown() / (float) airJumpComponent.getLastCooldown()) * 9));
					} else {
						graphics.blitSprite(RenderPipelines.GUI_TEXTURED, second, x, y, 9, 9);
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
