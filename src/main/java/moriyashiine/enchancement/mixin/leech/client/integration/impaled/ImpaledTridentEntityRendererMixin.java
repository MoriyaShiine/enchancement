package moriyashiine.enchancement.mixin.leech.client.integration.impaled;

import ladysnake.impaled.client.render.entity.ImpaledTridentEntityRenderer;
import ladysnake.impaled.client.render.entity.model.ImpaledTridentEntityModel;
import ladysnake.impaled.common.entity.ImpaledTridentEntity;
import moriyashiine.enchancement.client.util.EnchancementClientUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = ImpaledTridentEntityRenderer.class, remap = false)
public abstract class ImpaledTridentEntityRendererMixin extends EntityRenderer<ImpaledTridentEntity> {
	@Shadow
	@Final
	private ImpaledTridentEntityModel model;

	@Shadow
	public abstract Identifier getTexture(ImpaledTridentEntity impaledTridentEntity);

	protected ImpaledTridentEntityRendererMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Inject(method = "render(Lladysnake/impaled/common/entity/ImpaledTridentEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
	private void enchancement$leech(ImpaledTridentEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		EnchancementClientUtil.renderLeechTrident(entity, matrices, vertexConsumers, model, getTexture(entity), light, () -> super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light), ci);
	}
}
