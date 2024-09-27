/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.hidelabelbehindwalls.client;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
	@Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
	private void enchancement$hideLabelBehindWalls(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta, CallbackInfo ci) {
		if (MinecraftClient.getInstance().cameraEntity instanceof LivingEntity cameraEntity && entity instanceof LivingEntity living && !living.isGlowing() && EnchancementUtil.hasAnyEnchantmentsWith(living, ModEnchantmentEffectComponentTypes.HIDE_LABEL_BEHIND_WALLS) && !EnchancementUtil.hasAnyEnchantmentsWith(cameraEntity, ModEnchantmentEffectComponentTypes.ENTITY_XRAY) && !cameraEntity.canSee(living)) {
			ci.cancel();
		}
	}
}
