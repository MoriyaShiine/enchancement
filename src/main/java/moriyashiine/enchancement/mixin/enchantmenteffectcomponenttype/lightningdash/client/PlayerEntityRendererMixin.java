/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.lightningdash.client;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
	@Inject(method = "updateHandState", at = @At("TAIL"))
	private void enchancement$lightningDash(AbstractClientPlayerEntity player, PlayerEntityRenderState.HandState handState, Hand hand, CallbackInfo ci) {
		if (ModEntityComponents.LIGHTNING_DASH.get(player).isUsing()) {
			handState.itemUseAction = UseAction.SPEAR;
		}
	}
}
