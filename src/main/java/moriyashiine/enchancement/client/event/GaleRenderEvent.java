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
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.util.Identifier;

public class GaleRenderEvent implements HudRenderCallback {
	private static final MinecraftClient client = MinecraftClient.getInstance();

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
					RenderSystem.disableBlend();
				}
			}
		});
	}

	private static Identifier getTexture(int i) {
		Identifier id = Enchancement.id("hud/gale_" + i);
		if (client.getGuiAtlasManager().getSprite(id).equals(client.getGuiAtlasManager().getSprite(MissingSprite.getMissingSpriteId()))) {
			return Enchancement.id("hud/gale_blank");
		}
		return id;
	}
}
