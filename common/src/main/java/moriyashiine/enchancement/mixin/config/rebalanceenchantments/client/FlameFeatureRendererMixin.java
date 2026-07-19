package moriyashiine.enchancement.mixin.config.rebalanceenchantments.client;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.renderer.entity.state.EnchantedFireRenderState;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.client.resources.model.sprite.SpriteId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(FlameFeatureRenderer.class)
public class FlameFeatureRendererMixin {
	@Unique
	private static final SpriteId ENCHANTED_FIRE_0 = Sheets.BLOCKS_MAPPER.apply(Enchancement.id("enchanted_fire_0"));
	@Unique
	private static final SpriteId ENCHANTED_FIRE_1 = Sheets.BLOCKS_MAPPER.apply(Enchancement.id("enchanted_fire_1"));

	@Unique
	private static boolean shouldRenderEnchantedFire = false;

	@Inject(method = "renderSolid", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer;renderFlame(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lorg/joml/Quaternionf;Lnet/minecraft/client/resources/model/sprite/AtlasManager;)V"))
	private void enchancement$rebalanceEnchantments(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource, AtlasManager atlasManager, CallbackInfo ci, @Local(name = "flameSubmit") SubmitNodeStorage.FlameSubmit flameSubmit) {
		shouldRenderEnchantedFire = ((EnchantedFireRenderState.Submit) (Object) flameSubmit).enchancement$renderEnchantedFire();
	}

	@ModifyArg(method = "renderFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/sprite/AtlasManager;get(Lnet/minecraft/client/resources/model/sprite/SpriteId;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 0))
	private SpriteId enchancement$rebalanceEnchantments0(SpriteId sprite) {
		return shouldRenderEnchantedFire ? ENCHANTED_FIRE_0 : sprite;
	}

	@ModifyArg(method = "renderFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/sprite/AtlasManager;get(Lnet/minecraft/client/resources/model/sprite/SpriteId;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 1))
	private SpriteId enchancement$rebalanceEnchantments1(SpriteId sprite) {
		return shouldRenderEnchantedFire ? ENCHANTED_FIRE_1 : sprite;
	}
}
