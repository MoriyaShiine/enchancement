package moriyashiine.enchancement.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DashRenderEvent implements HudRenderCallback {
	private static final Identifier DASH_TEXTURE = new Identifier(Enchancement.MOD_ID, "textures/gui/dash.png");

	@Override
	public void onHudRender(MatrixStack matrices, float tickDelta) {
		MinecraftClient minecraft = MinecraftClient.getInstance();
		ModEntityComponents.DASH.maybeGet(minecraft.cameraEntity).ifPresent(dashComponent -> {
			if (dashComponent.hasDash() && dashComponent.getDashCooldown() > 0) {
				int scaledWidth = minecraft.getWindow().getScaledWidth(), scaledHeight = minecraft.getWindow().getScaledHeight();
				matrices.push();
				RenderSystem.setShaderTexture(0, DASH_TEXTURE);
				DrawableHelper.drawTexture(matrices, (int) (scaledWidth / 2F) - 8, (int) (scaledHeight / 2F) + 16, 0, 4, 16, 4, 16, 8);
				DrawableHelper.drawTexture(matrices, (int) (scaledWidth / 2F) - 8, (int) (scaledHeight / 2F) + 16, 0, 0, (int) (16 - (dashComponent.getDashCooldown() / 20F) * 16), 4, 16, 8);
				matrices.pop();
			}
		});
	}
}
