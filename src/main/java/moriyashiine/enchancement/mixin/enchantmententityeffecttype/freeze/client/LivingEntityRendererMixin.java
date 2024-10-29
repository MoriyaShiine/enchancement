/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmententityeffecttype.freeze.client;

import moriyashiine.enchancement.client.reloadlisteners.FrozenReloadListener;
import moriyashiine.enchancement.common.component.entity.FrozenComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> {
	@Unique
	private boolean isFrozen = false;

	protected LivingEntityRendererMixin(EntityRendererFactory.Context context) {
		super(context);
	}

	@ModifyVariable(method = "getRenderLayer", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getTexture(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;)Lnet/minecraft/util/Identifier;"))
	private Identifier enchancement$freeze(Identifier value) {
		if (isFrozen) {
			return FrozenReloadListener.INSTANCE.getTexture(value);
		}
		return value;
	}

	@Inject(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
	private void enchancement$freeze(T entity, S state, float tickDelta, CallbackInfo ci) {
		FrozenComponent frozenComponent = ModEntityComponents.FROZEN.get(entity);
		isFrozen = frozenComponent.isFrozen();
		if (isFrozen) {
			state.shaking = false;
			state.pose = frozenComponent.getForcedPose();
			state.yawDegrees = frozenComponent.getForcedHeadYaw();
			state.bodyYaw = frozenComponent.getForcedBodyYaw();
			state.pitch = frozenComponent.getForcedPitch();
			state.limbFrequency = frozenComponent.getForcedLimbFrequency();
			state.limbAmplitudeMultiplier = frozenComponent.getForcedLimbAmplitudeMultiplier();
			state.age = frozenComponent.getForcedClientAge();
		}
	}
}
