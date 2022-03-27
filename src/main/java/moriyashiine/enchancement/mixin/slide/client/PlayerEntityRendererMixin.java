package moriyashiine.enchancement.mixin.slide.client;

import moriyashiine.enchancement.common.registry.ModComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
	@Inject(method = "getPositionOffset(Lnet/minecraft/client/network/AbstractClientPlayerEntity;F)Lnet/minecraft/util/math/Vec3d;", at = @At("HEAD"), cancellable = true)
	private void enchancement$slide(AbstractClientPlayerEntity player, float f, CallbackInfoReturnable<Vec3d> cir) {
		if (ModComponents.SLIDE.get(player).shouldSlide()) {
			cir.setReturnValue(Vec3d.ZERO);
		}
	}

	@Inject(method = "setModelPose", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void enchancement$slide(AbstractClientPlayerEntity player, CallbackInfo ci, PlayerEntityModel<AbstractClientPlayerEntity> model) {
		if (ModComponents.SLIDE.get(player).shouldSlide()) {
			model.sneaking = false;
		}
	}
}
