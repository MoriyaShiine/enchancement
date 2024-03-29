/*
 * All Rights Reserved (c) MoriyaShiine
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
	private static final Identifier GALE_TEXTURE = Enchancement.id("textures/gui/gale.png");

	@Override
	public void onHudRender(DrawContext drawContext, float tickDelta) {
		ModEntityComponents.GALE.maybeGet(MinecraftClient.getInstance().cameraEntity).ifPresent(galeComponent -> {
			if (galeComponent.hasGale()) {
				int jumpsLeft = galeComponent.getJumpsLeft();
				if (jumpsLeft < galeComponent.getGaleLevel()) {
					RenderSystem.enableBlend();
					if (galeComponent.getGaleCooldown() < galeComponent.getLastGaleCooldown()) {
						drawContext.drawTexture(GALE_TEXTURE, (int) (drawContext.getScaledWindowWidth() / 2F) - 5, (int) (drawContext.getScaledWindowHeight() / 2F) + 27, 0, jumpsLeft == 0 ? 9 : 0, 9, 9, 9, 27);
						drawContext.drawTexture(GALE_TEXTURE, (int) (drawContext.getScaledWindowWidth() / 2F) - 5, (int) (drawContext.getScaledWindowHeight() / 2F) + 27, 0, jumpsLeft == 0 ? 18 : 9, 9, (int) ((galeComponent.getGaleCooldown() / (float) galeComponent.getLastGaleCooldown()) * 9), 9, 27);
					} else {
						drawContext.drawTexture(GALE_TEXTURE, (int) (drawContext.getScaledWindowWidth() / 2F) - 5, (int) (drawContext.getScaledWindowHeight() / 2F) + 27, 0, jumpsLeft == 0 ? 18 : 9, 9, 9, 9, 27);
					}
					RenderSystem.disableBlend();
				}
			}
		});
	}
}
