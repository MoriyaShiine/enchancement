/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class BouncyRenderEvent implements HudRenderCallback {
	private static final MinecraftClient client = MinecraftClient.getInstance();

	@Override
	public void onHudRender(MatrixStack matrixStack, float tickDelta) {
		ModEntityComponents.BOUNCY.maybeGet(client.getCameraEntity()).ifPresent(bouncyComponent -> {
			if (bouncyComponent.hasBouncy()) {
				float boostProgress = bouncyComponent.getBoostProgress();
				if (boostProgress > 0) {
					matrixStack.push();
					RenderSystem.setShaderTexture(0, DrawableHelper.GUI_ICONS_TEXTURE);
					int width = client.getWindow().getScaledWidth() / 2 - 91;
					int height = client.getWindow().getScaledHeight() - 32 + 3;
					DrawableHelper.drawTexture(matrixStack, width, height, 0, 84, 182, 5, 256, 256);
					DrawableHelper.drawTexture(matrixStack, client.getWindow().getScaledWidth() / 2 - 91, height, 0, 89, (int) (182 * boostProgress), 5, 256, 256);
					matrixStack.pop();
				}
			}
		});
	}
}
