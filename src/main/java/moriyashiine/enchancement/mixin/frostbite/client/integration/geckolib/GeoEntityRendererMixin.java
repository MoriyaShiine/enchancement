/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.frostbite.client.integration.geckolib;

import moriyashiine.enchancement.client.reloadlisteners.FrozenReloadListener;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(value = GeoEntityRenderer.class, remap = false)
public abstract class GeoEntityRendererMixin<T extends LivingEntity & IAnimatable> extends EntityRenderer<T> implements IGeoRenderer<T> {
	@Shadow
	@Final
	protected AnimatedGeoModel<T> modelProvider;

	@Shadow
	@Final
	protected List<GeoLayerRenderer<T>> layerRenderers;

	@Shadow
	protected abstract void applyRotations(T entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks);

	@Shadow
	public abstract Integer getUniqueID(T animatable);

	@Shadow
	protected abstract float handleRotationFloat(T livingBase, float partialTicks);

	@Shadow
	public static int getPackedOverlay(LivingEntity livingEntityIn, float uIn) {
		throw new UnsupportedOperationException();
	}

	protected GeoEntityRendererMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Inject(method = "getTextureLocation*", at = @At("RETURN"), cancellable = true)
	private void enchancement$frostbite(T entity, CallbackInfoReturnable<Identifier> cir) {
		if (ModEntityComponents.FROZEN.get(entity).isFrozen()) {
			cir.setReturnValue(FrozenReloadListener.INSTANCE.getTexture(cir.getReturnValue()));
		}
	}

	@Inject(method = "render*", at = @At("HEAD"), cancellable = true)
	private void enchancement$frostbite(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		ModEntityComponents.FROZEN.maybeGet(entity).ifPresent(frozenComponent -> {
			if (frozenComponent.isFrozen()) {
				matrices.push();
				EntityModelData entityModelData = new EntityModelData();
				entityModelData.isSitting = entity.hasVehicle();
				entityModelData.isChild = entity.isBaby();
				float bodyYaw = frozenComponent.getForcedBodyYaw();
				float pitch = frozenComponent.getForcedPitch();
				float headYawMinusBodyYaw = frozenComponent.getForcedHeadYaw() - bodyYaw;
				float limbDistance = Math.min(1, frozenComponent.getForcedLimbDistance());
				float limbAngleMinusLimbDistance = frozenComponent.getForcedLimbAngle() - frozenComponent.getForcedLimbDistance();
				float animationProgress = handleRotationFloat(entity, tickDelta);
				if (entity.isBaby()) {
					limbAngleMinusLimbDistance *= 3;
				}
				applyRotations(entity, matrices, animationProgress, bodyYaw, tickDelta);
				entityModelData.headPitch = -pitch;
				entityModelData.netHeadYaw = -headYawMinusBodyYaw;
				modelProvider.setLivingAnimations(entity, getUniqueID(entity), new AnimationEvent<>(entity, limbAngleMinusLimbDistance, limbDistance, tickDelta, !(limbDistance > -0.15F && limbDistance < 0.15F), Collections.singletonList(entityModelData)));
				matrices.translate(0, 0.01F, 0);
				MinecraftClient.getInstance().getTextureManager().bindTexture(getTexture(entity));
				if (!entity.isInvisibleTo(MinecraftClient.getInstance().player)) {
					Color renderColor = getRenderColor(entity, tickDelta, matrices, vertexConsumers, null, light);
					render(modelProvider.getModel(modelProvider.getModelLocation(entity)), entity, tickDelta, getRenderType(entity, tickDelta, matrices, vertexConsumers, null, light, getTexture(entity)), matrices, vertexConsumers, null, light, getPackedOverlay(entity, 0), (float) renderColor.getRed() / 255F, (float) renderColor.getGreen() / 255F, (float) renderColor.getBlue() / 255F, (float) renderColor.getAlpha() / 255F);
				}
				if (!entity.isSpectator()) {
					for (GeoLayerRenderer<T> layerRenderer : layerRenderers) {
						layerRenderer.render(matrices, vertexConsumers, light, entity, limbAngleMinusLimbDistance, limbDistance, tickDelta, animationProgress, headYawMinusBodyYaw, pitch);
					}
				}
				matrices.pop();
				super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
				ci.cancel();
			}
		});
	}
}
