/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment.client;

import moriyashiine.enchancement.client.render.entity.state.FloatingTridentRenderState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.entity.TridentEntityRenderer;
import net.minecraft.client.render.entity.state.TridentEntityRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntityRenderer.class)
public class TridentEntityRendererMixin {
	@Unique
	private final Random random = Random.create();

	@Unique
	private ItemRenderer itemRenderer;

	@Unique
	private FloatingTridentRenderState floatingTridentRenderState;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void enchancement$rebalanceEquipment(EntityRendererFactory.Context context, CallbackInfo ci) {
		itemRenderer = context.getItemRenderer();
		floatingTridentRenderState = new FloatingTridentRenderState();
	}

	@Inject(method = "render(Lnet/minecraft/client/render/entity/state/TridentEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
	private void enchancement$rebalanceEquipment(TridentEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if (floatingTridentRenderState.isFloating) {
			matrices.push();
			random.setSeed(ItemEntityRenderer.getSeed(floatingTridentRenderState.stack));
			matrices.translate(0.0F, MathHelper.sin(state.age / 10) * 0.1F + 0.1F + 0.25F * floatingTridentRenderState.model.getTransformation().getTransformation(ModelTransformationMode.GROUND).scale.y(), 0);
			matrices.multiply(RotationAxis.POSITIVE_Y.rotation(state.age / 20));
			ItemEntityRenderer.renderStack(itemRenderer, matrices, vertexConsumers, light, floatingTridentRenderState.stack, floatingTridentRenderState.model, floatingTridentRenderState.model.hasDepth(), random);
			matrices.pop();
			ci.cancel();
		}
	}

	@Inject(method = "updateRenderState(Lnet/minecraft/entity/projectile/TridentEntity;Lnet/minecraft/client/render/entity/state/TridentEntityRenderState;F)V", at = @At("TAIL"))
	private void enchancement$rebalanceEquipment(TridentEntity entity, TridentEntityRenderState state, float tickDelta, CallbackInfo ci) {
		floatingTridentRenderState.isFloating = entity.getDataTracker().get(TridentEntity.LOYALTY) > 0 && !entity.isOwnerAlive();
		floatingTridentRenderState.stack = entity.asItemStack();
		floatingTridentRenderState.model = itemRenderer.getModel(floatingTridentRenderState.stack, entity.getWorld(), null, entity.getId());
	}
}
