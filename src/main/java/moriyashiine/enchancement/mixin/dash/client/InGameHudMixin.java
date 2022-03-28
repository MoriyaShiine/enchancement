package moriyashiine.enchancement.mixin.dash.client;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.enchancement.common.registry.ModComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
	@Unique
	private static final Identifier DASH_TEXTURE = new Identifier("textures/mob_effect/speed.png");

	@Shadow
	private int scaledWidth;

	@Shadow
	private int scaledHeight;

	@Shadow
	protected abstract PlayerEntity getCameraPlayer();

	@Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0, shift = At.Shift.BEFORE))
	private void enchancement$dash(MatrixStack matrices, CallbackInfo ci) {
		ModComponents.DASH.maybeGet(getCameraPlayer()).ifPresent(dashComponent -> {
			if (dashComponent.getDashCooldown() > 0) {
				RenderSystem.setShaderTexture(0, DASH_TEXTURE);
				drawTexture(matrices, (int) (scaledWidth / 2F) - 9, (int) (scaledHeight / 2F) + 16, 0, 0, (int) (18 - (dashComponent.getDashCooldown() / 20F) * 18), 18, 18, 18);
				RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
			}
		});
	}
}
