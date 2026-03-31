/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.buryentity.client;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.renderer.entity.ShulkerRenderer;
import net.minecraft.client.renderer.entity.state.ShulkerRenderState;
import net.minecraft.world.entity.monster.Shulker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerRenderer.class)
public class ShulkerRendererMixin {
	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/monster/Shulker;Lnet/minecraft/client/renderer/entity/state/ShulkerRenderState;F)V", at = @At("TAIL"))
	private void enchancement$buryEntity(Shulker entity, ShulkerRenderState state, float partialTicks, CallbackInfo ci) {
		if (ModEntityComponents.BURY_ENTITY.get(entity).getBuryPos() != null) {
			state.renderOffset = state.renderOffset.add(0, -0.5, 0);
		}
	}
}
