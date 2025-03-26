/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmententityeffecttype.freeze.client;

import moriyashiine.enchancement.common.component.entity.FrozenSquidComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.render.entity.SquidEntityRenderer;
import net.minecraft.client.render.entity.state.SquidEntityRenderState;
import net.minecraft.entity.passive.SquidEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SquidEntityRenderer.class)
public class SquidEntityRendererMixin<T extends SquidEntity> {
	@Inject(method = "updateRenderState(Lnet/minecraft/entity/passive/SquidEntity;Lnet/minecraft/client/render/entity/state/SquidEntityRenderState;F)V", at = @At("TAIL"))
	private void enchancement$freeze(T entity, SquidEntityRenderState state, float tickProgress, CallbackInfo ci) {
		if (ModEntityComponents.FROZEN.get(entity).isFrozen()) {
			FrozenSquidComponent frozenSquidComponent = ModEntityComponents.FROZEN_SQUID.get(entity);
			state.tentacleAngle = frozenSquidComponent.getForcedTentacleAngle();
			state.tiltAngle = frozenSquidComponent.getForcedTiltAngle();
			state.rollAngle = frozenSquidComponent.getForcedRollAngle();
		}
	}
}
