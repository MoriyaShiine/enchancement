/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DashRenderEvent implements HudRenderCallback {
	private static final Identifier DASH_TEXTURE = Enchancement.id("textures/gui/dash.png");

	@Override
	public void onHudRender(DrawContext context, float tickDelta) {
		MinecraftClient minecraft = MinecraftClient.getInstance();
		ModEntityComponents.DASH.maybeGet(minecraft.cameraEntity).ifPresent(dashComponent -> {
			if (dashComponent.hasDash() && dashComponent.getDashCooldown() > 0) {
				int scaledWidth = minecraft.getWindow().getScaledWidth(), scaledHeight = minecraft.getWindow().getScaledHeight();
				RenderSystem.enableBlend();
				RenderSystem.setShaderTexture(0, DASH_TEXTURE);
				context.drawTexture(DASH_TEXTURE, (int) (scaledWidth / 2F) - 5, (int) (scaledHeight / 2F) + 18, 0, 4, 10, 4, 10, 8);
				if (dashComponent.getDashCooldown() < dashComponent.getLastDashCooldown()) {
					context.drawTexture(DASH_TEXTURE, (int) (scaledWidth / 2F) - 5, (int) (scaledHeight / 2F) + 18, 0, 0, (int) (11 - (dashComponent.getDashCooldown() / (float) dashComponent.getLastDashCooldown()) * 10), 4, 10, 8);
				}
				RenderSystem.disableBlend();
			}
		});
	}
}
