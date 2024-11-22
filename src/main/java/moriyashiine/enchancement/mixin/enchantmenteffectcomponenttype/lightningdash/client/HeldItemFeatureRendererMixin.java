/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.lightningdash.client;

import moriyashiine.enchancement.client.render.entity.state.EntityRenderStateAddition;
import moriyashiine.enchancement.common.enchantment.effect.LightningDashEffect;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemFeatureRenderer.class)
public class HeldItemFeatureRendererMixin<S extends LivingEntityRenderState> {
	@Inject(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V", ordinal = 0))
	private void enchancement$lightningDash(S state, @Nullable BakedModel model, ItemStack stack, ModelTransformationMode modelTransformation, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		EntityRenderStateAddition stateAddition = (EntityRenderStateAddition) state;
		if (ItemStack.areEqual(stateAddition.enchancement$getActiveStack(), stack) && LightningDashEffect.getChargeTime(stateAddition.enchancement$getRandom(), stack) != 0) {
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.age * 20));
		}
	}
}
