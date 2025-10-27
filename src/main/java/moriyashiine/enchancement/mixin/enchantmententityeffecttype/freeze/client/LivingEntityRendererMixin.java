/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmententityeffecttype.freeze.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.client.reloadlisteners.FrozenReloadListener;
import moriyashiine.enchancement.client.render.entity.state.FrozenRenderState;
import moriyashiine.enchancement.common.component.entity.FrozenComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState> extends EntityRenderer<T, S> {
	protected LivingEntityRendererMixin(EntityRendererFactory.Context context) {
		super(context);
	}

	@ModifyExpressionValue(method = "getRenderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getTexture(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;)Lnet/minecraft/util/Identifier;"))
	private Identifier enchancement$freeze(Identifier original, S state) {
		@Nullable FrozenRenderState frozenRenderState = state.getData(FrozenRenderState.KEY);
		if (frozenRenderState != null && frozenRenderState.frozen) {
			return FrozenReloadListener.INSTANCE.getTexture(original);
		}
		return original;
	}

	@Inject(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
	private void enchancement$freeze(T entity, S state, float tickProgress, CallbackInfo ci) {
		FrozenRenderState frozenRenderState = new FrozenRenderState();
		FrozenComponent frozenComponent = ModEntityComponents.FROZEN.get(entity);
		if (frozenComponent.isFrozen()) {
			frozenRenderState.frozen = true;
			state.shaking = false;
			state.pose = frozenComponent.getForcedPose();
			state.relativeHeadYaw = frozenComponent.getForcedHeadYaw();
			state.bodyYaw = frozenComponent.getForcedBodyYaw();
			state.pitch = frozenComponent.getForcedPitch();
			state.limbSwingAnimationProgress = frozenComponent.getForcedLimbSwingAnimationProgress();
			state.limbSwingAmplitude = frozenComponent.getForcedLimbSwingAmplitude();
			state.age = frozenComponent.getForcedClientAge();
		}
		state.setData(FrozenRenderState.KEY, frozenRenderState);
	}
}
