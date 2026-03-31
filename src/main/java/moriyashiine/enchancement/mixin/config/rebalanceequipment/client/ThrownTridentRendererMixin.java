/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceequipment.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import moriyashiine.enchancement.client.renderer.entity.state.FloatingTridentRenderState;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// todo make the glint work here
@Mixin(ThrownTridentRenderer.class)
public class ThrownTridentRendererMixin {
	@Unique
	private final RandomSource random = RandomSource.create();

	@Unique
	private ItemModelResolver itemModelResolver;

	@Unique
	private FloatingTridentRenderState floatingTridentRenderState;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void enchancement$rebalanceEquipment(EntityRendererProvider.Context context, CallbackInfo ci) {
		itemModelResolver = context.getItemModelResolver();
		floatingTridentRenderState = new FloatingTridentRenderState();
	}

	@Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At("HEAD"), cancellable = true)
	private void enchancement$rebalanceEquipment(ThrownTridentRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
		if (floatingTridentRenderState.floating) {
			poseStack.pushPose();
			poseStack.translate(0.0F, Mth.sin(state.ageInTicks / 10) * 0.1F + 0.1F + 0.25F, 0);
			poseStack.mulPose(Axis.YP.rotation(ItemEntity.getSpin(state.ageInTicks, 0)));
			ItemEntityRenderer.submitMultipleFromCount(poseStack, submitNodeCollector, state.lightCoords, floatingTridentRenderState.item, random);
			poseStack.popPose();
			ci.cancel();
		}
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/projectile/arrow/ThrownTrident;Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;F)V", at = @At("TAIL"))
	private void enchancement$rebalanceEquipment(ThrownTrident entity, ThrownTridentRenderState state, float partialTicks, CallbackInfo ci) {
		floatingTridentRenderState.floating = ModConfig.rebalanceEquipment && entity.getEntityData().get(ThrownTrident.ID_LOYALTY) > 0 && !entity.isAcceptibleReturnOwner() && ModEntityComponents.OWNED_TRIDENT.get(entity).isOwnedByPlayer() && ModEntityComponents.LEECHING_TRIDENT.get(entity).getStuckEntity() == null;
		floatingTridentRenderState.item.extractItemGroupRenderState(entity, entity.getPickupItem(), itemModelResolver);
	}
}
