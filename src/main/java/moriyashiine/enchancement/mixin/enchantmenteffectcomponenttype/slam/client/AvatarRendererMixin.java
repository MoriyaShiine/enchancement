/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.slam.client;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.SlamComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> {
	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
	private void enchancement$slam(AvatarlikeEntity entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
		@Nullable SlamComponent slamComponent = ModEntityComponents.SLAM.getNullable(entity);
		if (slamComponent != null && slamComponent.isSlamming()) {
			state.isCrouching = state.isDiscrete = true;
		}
	}
}