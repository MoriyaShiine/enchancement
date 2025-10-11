/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.slam.client;

import moriyashiine.enchancement.common.component.entity.SlamComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.network.ClientPlayerLikeEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.PlayerLikeEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin<AvatarlikeEntity extends PlayerLikeEntity & ClientPlayerLikeEntity> {
	@Inject(method = "updateRenderState(Lnet/minecraft/entity/PlayerLikeEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V", at = @At("TAIL"))
	private void enchancement$slam(AvatarlikeEntity entity, PlayerEntityRenderState state, float tickProgress, CallbackInfo ci) {
		@Nullable SlamComponent slamComponent = ModEntityComponents.SLAM.getNullable(entity);
		if (slamComponent != null && slamComponent.isSlamming()) {
			state.isInSneakingPose = state.sneaking = true;
		}
	}
}