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
public class GaleRenderEvent implements HudRenderCallback {
	private static final Identifier GALE_TEXTURE = Enchancement.id("textures/gui/gale.png");

	@Override
	public void onHudRender(DrawContext context, float tickDelta) {
		MinecraftClient minecraft = MinecraftClient.getInstance();
		ModEntityComponents.GALE.maybeGet(minecraft.cameraEntity).ifPresent(galeComponent -> {
			if (galeComponent.hasGale()) {
				int jumpsLeft = galeComponent.getJumpsLeft();
				if (jumpsLeft < 2) {
					int scaledWidth = minecraft.getWindow().getScaledWidth(), scaledHeight = minecraft.getWindow().getScaledHeight();
					RenderSystem.enableBlend();
					RenderSystem.setShaderTexture(0, GALE_TEXTURE);
					context.drawTexture(GALE_TEXTURE, (int) (scaledWidth / 2F) - 5, (int) (scaledHeight / 2F) + 27, 0, jumpsLeft == 1 ? 0 : 9, 9, 9, 9, 18);
					RenderSystem.disableBlend();
				}
			}
		});
	}
}
