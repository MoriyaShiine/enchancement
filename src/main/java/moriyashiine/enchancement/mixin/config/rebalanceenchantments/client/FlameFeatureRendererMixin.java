/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceenchantments.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import moriyashiine.enchancement.client.renderer.entity.state.EnchantedFireRenderState;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(FlameFeatureRenderer.class)
public class FlameFeatureRendererMixin {
	@Unique
	private static final SpriteId ENCHANTED_FIRE_0 = Sheets.BLOCKS_MAPPER.apply(Enchancement.id("enchanted_fire_0"));
	@Unique
	private static final SpriteId ENCHANTED_FIRE_1 = Sheets.BLOCKS_MAPPER.apply(Enchancement.id("enchanted_fire_1"));

	@WrapOperation(method = "buildGroup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer;prepare(Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"))
	private void enchancement$rebalanceEnchantments(FlameFeatureRenderer instance, FlameFeatureRenderer.Submit submit, VertexConsumer buffer, TextureAtlasSprite fire1, TextureAtlasSprite fire2, Operation<Void> original, FeatureFrameContext context) {
		EnchantedFireRenderState enchantedFireRenderState = submit.entityRenderState().getData(EnchantedFireRenderState.KEY);
		if (enchantedFireRenderState != null && enchantedFireRenderState.renderEnchantedFire) {
			fire1 = context.atlasManager().get(ENCHANTED_FIRE_0);
			fire2 = context.atlasManager().get(ENCHANTED_FIRE_1);
		}
		original.call(instance, submit, buffer, fire1, fire2);
	}
}
