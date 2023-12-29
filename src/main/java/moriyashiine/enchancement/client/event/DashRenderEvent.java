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

public class DashRenderEvent implements HudRenderCallback {
	private static final Identifier DASH_TEXTURE = Enchancement.id("textures/gui/dash.png");

	@Override
	public void onHudRender(DrawContext drawContext, float tickDelta) {
		ModEntityComponents.DASH.maybeGet(MinecraftClient.getInstance().cameraEntity).ifPresent(dashComponent -> {
			if (dashComponent.hasDash() && dashComponent.getDashCooldown() > 0) {
				RenderSystem.enableBlend();
				drawContext.drawTexture(DASH_TEXTURE, (int) (drawContext.getScaledWindowWidth() / 2F) - 5, (int) (drawContext.getScaledWindowHeight() / 2F) + 18, 0, 4, 10, 4, 10, 8);
				if (dashComponent.getDashCooldown() < dashComponent.getLastDashCooldown()) {
					drawContext.drawTexture(DASH_TEXTURE, (int) (drawContext.getScaledWindowWidth() / 2F) - 5, (int) (drawContext.getScaledWindowHeight() / 2F) + 18, 0, 0, (int) (11 - (dashComponent.getDashCooldown() / (float) dashComponent.getLastDashCooldown()) * 10), 4, 10, 8);
				}
				RenderSystem.disableBlend();
			}
		});
	}
}
