/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.leechingtrident.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.client.render.entity.state.LeechingTridentRenderState;
import moriyashiine.enchancement.common.component.entity.LeechingTridentComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TridentEntityRenderer;
import net.minecraft.client.render.entity.state.TridentEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntityRenderer.class)
public abstract class TridentEntityRendererMixin extends EntityRenderer<TridentEntity, TridentEntityRenderState> {
	@Unique
	private final LeechingTridentRenderState leechingTridentRenderState = new LeechingTridentRenderState();

	protected TridentEntityRendererMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@WrapWithCondition(method = "render(Lnet/minecraft/client/render/entity/state/TridentEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V"))
	private boolean enchancement$leechingTrident(MatrixStack instance, Quaternionf quaternion) {
		return leechingTridentRenderState.stuckEntity == null;
	}

	@Inject(method = "render(Lnet/minecraft/client/render/entity/state/TridentEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V", ordinal = 0))
	private void enchancement$leechingTrident(TridentEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if (leechingTridentRenderState.stuckEntity != null) {
			float offsetX = MathHelper.sin(leechingTridentRenderState.leechingTicks), offsetZ = MathHelper.cos(leechingTridentRenderState.leechingTicks);
			matrices.translate(offsetX, 0, offsetZ);
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) -MathHelper.wrapDegrees((MathHelper.atan2(leechingTridentRenderState.stuckEntity.getZ() - leechingTridentRenderState.trident.getZ() + offsetZ, leechingTridentRenderState.stuckEntity.getX() - leechingTridentRenderState.trident.getX() + offsetX) * 57.2957763671875) - 90) + 90));
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(60));
			matrices.translate(0, -leechingTridentRenderState.stabTicks, 0);
		}
	}

	@Inject(method = "updateRenderState(Lnet/minecraft/entity/projectile/TridentEntity;Lnet/minecraft/client/render/entity/state/TridentEntityRenderState;F)V", at = @At("TAIL"))
	private void enchancement$leechingTrident(TridentEntity entity, TridentEntityRenderState state, float tickDelta, CallbackInfo ci) {
		LeechingTridentComponent leechingTridentComponent = ModEntityComponents.LEECHING_TRIDENT.get(entity);
		leechingTridentRenderState.trident = entity;
		leechingTridentRenderState.stuckEntity = leechingTridentComponent.getStuckEntity();
		leechingTridentRenderState.leechingTicks = Math.max(0, (leechingTridentComponent.getLeechingTicks() + tickDelta) / 20F);
		leechingTridentRenderState.stabTicks = leechingTridentComponent.getStabTicks() / 20F;
	}
}
