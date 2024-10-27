/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.entity.TridentEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntityRenderer.class)
public class TridentEntityRendererMixin {
	@Unique
	private static final MinecraftClient client = MinecraftClient.getInstance();
	@Unique
	private final Random random = Random.create();

	@Inject(method = "render(Lnet/minecraft/entity/projectile/TridentEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
	private void enchancement$rebalanceEquipment(TridentEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if (entity.getDataTracker().get(TridentEntity.LOYALTY) > 0 && !entity.isOwnerAlive()) {
			matrices.push();
			ItemStack itemStack = entity.asItemStack();
			random.setSeed(ItemEntityRenderer.getSeed(itemStack));
			BakedModel model = client.getItemRenderer().getModel(itemStack, entity.getWorld(), null, entity.getId());
			matrices.translate(0, MathHelper.sin((entity.age + tickDelta) / 10) * 0.1F + 0.1F + 0.25F * model.getTransformation().getTransformation(ModelTransformationMode.GROUND).scale.y(), 0);
			matrices.multiply(RotationAxis.POSITIVE_Y.rotation((entity.age + tickDelta) / 20));
			ItemEntityRenderer.renderStack(client.getItemRenderer(), matrices, vertexConsumers, light, itemStack, model, model.hasDepth(), random);
			matrices.pop();
			ci.cancel();
		}
	}
}
