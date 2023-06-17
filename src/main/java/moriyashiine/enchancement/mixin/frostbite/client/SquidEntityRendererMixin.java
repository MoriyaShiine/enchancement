/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.frostbite.client;

import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.SquidEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SquidEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(SquidEntityRenderer.class)
public class SquidEntityRendererMixin<T extends SquidEntity> {
	@ModifyVariable(method = "setupTransforms(Lnet/minecraft/entity/passive/SquidEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F", ordinal = 0), ordinal = 3)
	private float enchancement$frostbiteTiltAngle(float value, T entity) {
		if (ModEntityComponents.FROZEN.get(entity).isFrozen()) {
			return ModEntityComponents.FROZEN_SQUID.get(entity).getForcedTiltAngle();
		}
		return value;
	}

	@ModifyVariable(method = "setupTransforms(Lnet/minecraft/entity/passive/SquidEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F", ordinal = 1), ordinal = 4)
	private float enchancement$frostbiteRollAngle(float value, T squidEntity, MatrixStack matrixStack, float f, float g, float h) {
		if (ModEntityComponents.FROZEN.get(squidEntity).isFrozen()) {
			return ModEntityComponents.FROZEN_SQUID.get(squidEntity).getForcedRollAngle();
		}
		return value;
	}

	@Inject(method = "getAnimationProgress(Lnet/minecraft/entity/passive/SquidEntity;F)F", at = @At("HEAD"), cancellable = true)
	private void enchancement$frostbite(T entity, float tickDelta, CallbackInfoReturnable<Float> cir) {
		if (ModEntityComponents.FROZEN.get(entity).isFrozen()) {
			cir.setReturnValue(ModEntityComponents.FROZEN_SQUID.get(entity).getForcedTentacleAngle());
		}
	}
}
