/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rage.client;

import com.mojang.blaze3d.vertex.PoseStack;
import moriyashiine.enchancement.client.renderer.item.state.RageRenderState;
import moriyashiine.enchancement.common.world.item.effects.RageEffect;
import net.fabricmc.fabric.api.client.rendering.v1.FabricRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStackRenderState.class)
public class ItemStackRenderStateMixin {
	@Inject(method = "submit", at = @At("HEAD"))
	private void enchancement$rage(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, int outlineColor, CallbackInfo ci) {
		int rageColor = -1;
		@Nullable RageRenderState rageRenderState = ((FabricRenderState) this).getData(RageRenderState.KEY);
		if (rageRenderState != null) {
			rageColor = rageRenderState.color;
		}
		RageEffect.color = rageColor;
	}

	@Inject(method = "submit", at = @At("TAIL"))
	private void enchancement$rageTail(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, int outlineColor, CallbackInfo ci) {
		RageEffect.color = -1;
	}
}
