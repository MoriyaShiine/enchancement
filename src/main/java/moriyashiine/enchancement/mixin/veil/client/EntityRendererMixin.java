/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.veil.client;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
	@Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
	private void enchancement$veil(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if (MinecraftClient.getInstance().cameraEntity instanceof LivingEntity cameraEntity && entity instanceof LivingEntity living && !living.isGlowing() && EnchancementUtil.hasEnchantment(ModEnchantments.VEIL, living) && !EnchancementUtil.hasEnchantment(ModEnchantments.PERCEPTION, cameraEntity) && !cameraEntity.canSee(living)) {
			ci.cancel();
		}
	}
}
