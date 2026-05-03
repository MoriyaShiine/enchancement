/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.enhancemobs.tridentspinattack.client;

import moriyashiine.enchancement.client.renderer.entity.layer.MobSpinAttackEffectLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
	@Shadow
	protected abstract boolean addLayer(RenderLayer<S, M> layer);

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Inject(method = "<init>", at = @At("TAIL"))
	private void enchancement$enhanceMobs(EntityRendererProvider.Context context, M model, float shadow, CallbackInfo ci) {
		LivingEntityRenderer renderer = (LivingEntityRenderer) (Object) this;
		if (!(renderer instanceof AvatarRenderer<?>)) {
			addLayer(new MobSpinAttackEffectLayer<>(renderer, context.getModelSet()));
		}
	}
}
