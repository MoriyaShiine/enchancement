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
import net.minecraft.util.Identifier;

public class GaleRenderEvent implements HudRenderCallback {
	private static final Identifier[] TEXTURES = new Identifier[16];

	static {
		for (int i = 0; i < TEXTURES.length; i++) {
			TEXTURES[i] = Enchancement.id("hud/gale_" + i);
		}
	}

	@Override
	public void onHudRender(DrawContext drawContext, float tickDelta) {
		ModEntityComponents.GALE.maybeGet(MinecraftClient.getInstance().cameraEntity).ifPresent(galeComponent -> {
			if (galeComponent.hasGale()) {
				int jumpsLeft = galeComponent.getJumpsLeft();
				if (jumpsLeft < galeComponent.getGaleLevel()) {
					RenderSystem.enableBlend();
					Identifier first = getTexture(jumpsLeft + 1);
					Identifier second = getTexture(jumpsLeft);
					int x = drawContext.getScaledWindowWidth() / 2 - 5, y = drawContext.getScaledWindowHeight() / 2 + 27;
					if (galeComponent.getGaleCooldown() < galeComponent.getLastGaleCooldown()) {
						drawContext.drawGuiTexture(first, x, y, 9, 9);
						drawContext.drawGuiTexture(second, 9, 9, 0, 0, x, y, 9, (int) ((galeComponent.getGaleCooldown() / (float) galeComponent.getLastGaleCooldown()) * 9));
					} else {
						drawContext.drawGuiTexture(second, x, y, 9, 9);
					}
					drawContext.setShaderColor(1, 1, 1, 1);
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
