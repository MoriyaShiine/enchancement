/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.hidelabelbehindwalls.client;

import com.mojang.blaze3d.vertex.PoseStack;
import moriyashiine.enchancement.client.renderer.entity.state.ExtraRenderState;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<S extends EntityRenderState> {
	@Inject(method = "submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;I)V", at = @At("HEAD"), cancellable = true)
	private void enchancement$hideLabelBehindWalls(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, int offset, CallbackInfo ci) {
		Player player = Minecraft.getInstance().player;
		if (player != null && !player.isSpectator()) {
			@Nullable ExtraRenderState extraRenderState = state.getData(ExtraRenderState.KEY);
			if (extraRenderState != null) {
				if (extraRenderState.canCameraSee && extraRenderState.hidesNameDisplays && !extraRenderState.glowing && !EnchancementUtil.hasAnyEnchantmentsWith(player, ModEnchantmentEffectComponentTypes.ENTITY_XRAY)) {
					ci.cancel();
				}
			}
		}
	}
}
