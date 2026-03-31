/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.leechingtrident.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import moriyashiine.enchancement.client.renderer.entity.state.LeechingTridentRenderState;
import moriyashiine.enchancement.common.component.entity.LeechingTridentComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTridentRenderer.class)
public abstract class ThrownTridentRendererMixin extends EntityRenderer<ThrownTrident, ThrownTridentRenderState> {
	@Unique
	private final LeechingTridentRenderState leechingTridentRenderState = new LeechingTridentRenderState();

	protected ThrownTridentRendererMixin(EntityRendererProvider.Context context) {
		super(context);
	}

	@WrapWithCondition(method = "submit(Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V"))
	private boolean enchancement$leechingTrident(PoseStack instance, Quaternionfc by) {
		return !leechingTridentRenderState.active;
	}

	@Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V", ordinal = 0))
	private void enchancement$leechingTrident(ThrownTridentRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
		if (leechingTridentRenderState.active) {
			poseStack.translate(leechingTridentRenderState.offsetX, 0, leechingTridentRenderState.offsetZ);
			poseStack.mulPose(Axis.YP.rotationDegrees(leechingTridentRenderState.rotationY));
			poseStack.mulPose(Axis.ZP.rotationDegrees(60));
			poseStack.translate(0, -leechingTridentRenderState.stabTicks, 0);
		}
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/projectile/arrow/ThrownTrident;Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;F)V", at = @At("TAIL"))
	private void enchancement$leechingTrident(ThrownTrident entity, ThrownTridentRenderState state, float partialTicks, CallbackInfo ci) {
		LeechingTridentComponent leechingTridentComponent = ModEntityComponents.LEECHING_TRIDENT.get(entity);
		LivingEntity stuckEntity = leechingTridentComponent.getStuckEntity();
		if (stuckEntity == null) {
			leechingTridentRenderState.active = false;
			leechingTridentRenderState.offsetX = leechingTridentRenderState.offsetZ = leechingTridentRenderState.rotationY = leechingTridentRenderState.stabTicks = 0;
		} else {
			leechingTridentRenderState.active = true;
			float leechingTicks = Math.max(0, (leechingTridentComponent.getLeechingTicks() + partialTicks) / 20F);
			leechingTridentRenderState.offsetX = Mth.sin(leechingTicks);
			leechingTridentRenderState.offsetZ = Mth.cos(leechingTicks);
			leechingTridentRenderState.rotationY = (float) -Mth.wrapDegrees((Mth.atan2(stuckEntity.getZ() - entity.getZ() + leechingTridentRenderState.offsetZ, stuckEntity.getX() - entity.getX() + leechingTridentRenderState.offsetX) * 57.2957763671875) - 90) + 90;
			leechingTridentRenderState.stabTicks = leechingTridentComponent.getStabTicks() / 20F;
		}
	}
}
