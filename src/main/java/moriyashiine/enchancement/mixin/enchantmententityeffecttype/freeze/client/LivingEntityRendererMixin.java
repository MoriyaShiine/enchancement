/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmententityeffecttype.freeze.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.client.reloadlistener.FrozenReloadListener;
import moriyashiine.enchancement.client.renderer.entity.state.FrozenRenderState;
import moriyashiine.enchancement.common.component.entity.FrozenComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState> extends EntityRenderer<T, S> {
	protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
		super(context);
	}

	@ModifyExpressionValue(method = "getRenderType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)Lnet/minecraft/resources/Identifier;"))
	private Identifier enchancement$freeze(Identifier original, S state) {
		@Nullable FrozenRenderState frozenRenderState = state.getData(FrozenRenderState.KEY);
		if (frozenRenderState != null && frozenRenderState.frozen) {
			return FrozenReloadListener.INSTANCE.getTexture(original);
		}
		return original;
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
	private void enchancement$freeze(T entity, S state, float partialTicks, CallbackInfo ci) {
		FrozenRenderState frozenRenderState = new FrozenRenderState();
		FrozenComponent frozenComponent = ModEntityComponents.FROZEN.get(entity);
		if (frozenComponent.isFrozen()) {
			frozenRenderState.frozen = true;
			state.isFullyFrozen = false;
			state.pose = frozenComponent.getForcedPose();
			state.yRot = frozenComponent.getForcedYHeadRot();
			state.bodyRot = frozenComponent.getForcedVisualRotationYInDegrees();
			state.xRot = frozenComponent.getForcedXRot();
			state.walkAnimationPos = frozenComponent.getForcedWalkAnimationPos();
			state.walkAnimationSpeed = frozenComponent.getForcedWalkAnimationSpeed();
			state.ageInTicks = frozenComponent.getForcedClientTickCount();
		}
		state.setData(FrozenRenderState.KEY, frozenRenderState);
	}
}
