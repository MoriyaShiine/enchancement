/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.lightningdash.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import moriyashiine.enchancement.client.renderer.entity.state.ExtraRenderState;
import moriyashiine.enchancement.common.world.item.effects.LightningDashEffect;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin<S extends ArmedEntityRenderState> {
	@Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V", ordinal = 0))
	private void enchancement$lightningDash(S state, ItemStackRenderState item, ItemStack itemStack, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
		if (arm == state.mainArm) {
			@Nullable ExtraRenderState extraRenderState = state.getData(ExtraRenderState.KEY);
			if (extraRenderState != null && extraRenderState.random != null) {
				if (ItemStack.matches(extraRenderState.activeStack, itemStack) && LightningDashEffect.getFloatTime(extraRenderState.random, itemStack) != 0) {
					poseStack.mulPose(Axis.YP.rotationDegrees(state.ageInTicks * 20));
				}
			}
		}
	}
}
