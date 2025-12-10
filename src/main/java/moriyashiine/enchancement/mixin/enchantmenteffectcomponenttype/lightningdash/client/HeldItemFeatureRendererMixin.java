/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.lightningdash.client;

import moriyashiine.enchancement.client.render.entity.state.ExtraRenderState;
import moriyashiine.enchancement.common.enchantment.effect.LightningDashEffect;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemFeatureRenderer.class)
public class HeldItemFeatureRendererMixin<S extends ArmedEntityRenderState> {
	@Inject(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionfc;)V", ordinal = 0))
	private void enchancement$lightningDash(S entityState, ItemRenderState itemRenderState, ItemStack itemStack, Arm arm, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, int i, CallbackInfo ci) {
		if (arm == entityState.mainArm) {
			@Nullable ExtraRenderState extraRenderState = entityState.getData(ExtraRenderState.KEY);
			if (extraRenderState != null && extraRenderState.random != null) {
				if (ItemStack.areEqual(extraRenderState.activeStack, itemStack) && LightningDashEffect.getFloatTime(extraRenderState.random, itemStack) != 0) {
					matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entityState.age * 20));
				}
			}
		}
	}
}
