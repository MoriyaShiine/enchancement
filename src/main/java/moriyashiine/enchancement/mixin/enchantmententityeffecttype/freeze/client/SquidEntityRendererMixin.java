/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmententityeffecttype.freeze.client;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.render.entity.SquidEntityRenderer;
import net.minecraft.entity.passive.SquidEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SquidEntityRenderer.class)
public class SquidEntityRendererMixin<T extends SquidEntity> {
	@ModifyVariable(method = "setupTransforms(Lnet/minecraft/entity/passive/SquidEntity;Lnet/minecraft/client/util/math/MatrixStack;FFFF)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F", ordinal = 0), ordinal = 3, argsOnly = true)
	private float enchancement$freezeTiltAngle(float value, T entity) {
		if (ModEntityComponents.FROZEN.get(entity).isFrozen()) {
			return ModEntityComponents.FROZEN_SQUID.get(entity).getForcedTiltAngle();
		}
		return value;
	}

	@ModifyVariable(method = "setupTransforms(Lnet/minecraft/entity/passive/SquidEntity;Lnet/minecraft/client/util/math/MatrixStack;FFFF)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F", ordinal = 1), ordinal = 4)
	private float enchancement$freezeRollAngle(float value, T entity) {
		if (ModEntityComponents.FROZEN.get(entity).isFrozen()) {
			return ModEntityComponents.FROZEN_SQUID.get(entity).getForcedRollAngle();
		}
		return value;
	}

	@Inject(method = "getAnimationProgress(Lnet/minecraft/entity/passive/SquidEntity;F)F", at = @At("HEAD"), cancellable = true)
	private void enchancement$freeze(T entity, float tickDelta, CallbackInfoReturnable<Float> cir) {
		if (ModEntityComponents.FROZEN.get(entity).isFrozen()) {
			cir.setReturnValue(ModEntityComponents.FROZEN_SQUID.get(entity).getForcedTentacleAngle());
		}
	}
}
