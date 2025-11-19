/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment.client;

import moriyashiine.enchancement.client.render.entity.state.FloatingTridentRenderState;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.entity.TridentEntityRenderer;
import net.minecraft.client.render.entity.state.TridentEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// todo make the glint work here
@Mixin(TridentEntityRenderer.class)
public class TridentEntityRendererMixin {
	@Unique
	private final Random random = Random.create();

	@Unique
	private ItemModelManager itemModelManager;

	@Unique
	private FloatingTridentRenderState floatingTridentRenderState;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void enchancement$rebalanceEquipment(EntityRendererFactory.Context context, CallbackInfo ci) {
		itemModelManager = context.getItemModelManager();
		floatingTridentRenderState = new FloatingTridentRenderState();
	}

	@Inject(method = "render(Lnet/minecraft/client/render/entity/state/TridentEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V", at = @At("HEAD"), cancellable = true)
	private void enchancement$rebalanceEquipment(TridentEntityRenderState renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState, CallbackInfo ci) {
		if (floatingTridentRenderState.floating) {
			matrices.push();
			matrices.translate(0.0F, MathHelper.sin(renderState.age / 10) * 0.1F + 0.1F + 0.25F, 0);
			matrices.multiply(RotationAxis.POSITIVE_Y.rotation(ItemEntity.getRotation(renderState.age, 0)));
			ItemEntityRenderer.render(matrices, queue, renderState.light, floatingTridentRenderState.stackState, random);
			matrices.pop();
			ci.cancel();
		}
	}

	@Inject(method = "updateRenderState(Lnet/minecraft/entity/projectile/TridentEntity;Lnet/minecraft/client/render/entity/state/TridentEntityRenderState;F)V", at = @At("TAIL"))
	private void enchancement$rebalanceEquipment(TridentEntity entity, TridentEntityRenderState state, float tickProgress, CallbackInfo ci) {
		floatingTridentRenderState.floating = ModConfig.rebalanceEquipment && entity.getDataTracker().get(TridentEntity.LOYALTY) > 0 && !entity.isOwnerAlive() && ModEntityComponents.OWNED_TRIDENT.get(entity).isOwnedByPlayer() && ModEntityComponents.LEECHING_TRIDENT.get(entity).getStuckEntity() == null;
		floatingTridentRenderState.stackState.update(entity, entity.asItemStack(), itemModelManager);
	}
}
