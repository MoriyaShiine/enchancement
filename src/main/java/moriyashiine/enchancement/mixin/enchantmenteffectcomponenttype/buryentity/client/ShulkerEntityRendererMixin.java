/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.buryentity.client;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.render.entity.ShulkerEntityRenderer;
import net.minecraft.client.render.entity.state.ShulkerEntityRenderState;
import net.minecraft.entity.mob.ShulkerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerEntityRenderer.class)
public class ShulkerEntityRendererMixin {
	@Inject(method = "updateRenderState(Lnet/minecraft/entity/mob/ShulkerEntity;Lnet/minecraft/client/render/entity/state/ShulkerEntityRenderState;F)V", at = @At("TAIL"))
	private void enchancement$buryEntity(ShulkerEntity entity, ShulkerEntityRenderState state, float tickDelta, CallbackInfo ci) {
		if (ModEntityComponents.BURY_ENTITY.get(entity).getBuryPos() != null) {
			state.renderPositionOffset = state.renderPositionOffset.add(0, -0.5, 0);
		}
	}
}
