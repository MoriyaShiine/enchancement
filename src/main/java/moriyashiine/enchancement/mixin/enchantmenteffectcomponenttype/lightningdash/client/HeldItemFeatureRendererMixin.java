/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.lightningdash.client;

import moriyashiine.enchancement.client.render.entity.state.EntityRenderStateAddition;
import moriyashiine.enchancement.common.enchantment.effect.LightningDashEffect;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemFeatureRenderer.class)
public class HeldItemFeatureRendererMixin<S extends ArmedEntityRenderState> {
	@Inject(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionfc;)V", ordinal = 0))
	private void enchancement$lightningDash(S entityState, ItemRenderState itemState, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if (arm == entityState.mainArm) {
			EntityRenderStateAddition stateAddition = (EntityRenderStateAddition) entityState;
			if (ItemStack.areEqual(stateAddition.enchancement$getActiveStack(), stateAddition.enchancement$getMainHandStack()) && LightningDashEffect.getChargeTime(stateAddition.enchancement$getRandom(), stateAddition.enchancement$getMainHandStack()) != 0) {
				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entityState.age * 20));
			}
		}
	}
}
