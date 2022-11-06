/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.frostbite.client.integration.geckolib;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.enchancement.client.reloadlisteners.FrozenReloadListener;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumers;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
import software.bernie.geckolib3.util.EModelRenderCycle;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(value = GeoEntityRenderer.class, remap = false)
public abstract class GeoEntityRendererMixin<T extends LivingEntity & IAnimatable> extends EntityRenderer<T> implements IGeoRenderer<T> {
	@Shadow
	@Final
	protected List<GeoLayerRenderer<T>> layerRenderers;

	@Shadow
	@Final
	protected AnimatedGeoModel<T> modelProvider;

	@Shadow
	protected Matrix4f dispatchedMat;

	@Shadow
	protected abstract void applyRotations(T animatable, MatrixStack poseStack, float ageInTicks, float rotationYaw, float partialTick);

	@Shadow
	public abstract int getOverlay(T entity, float u);

	@Shadow
	protected abstract float getSwingMotionAnimThreshold();

	@Shadow
	protected abstract void renderLayer(MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, T animatable, float limbSwing, float limbSwingAmount, float partialTick, float rotFloat, float netHeadYaw, float headPitch, VertexConsumerProvider bufferSource2, GeoLayerRenderer<T> layerRenderer);

	protected GeoEntityRendererMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Inject(method = "getTextureLocation*", at = @At("RETURN"), cancellable = true)
	private void enchancement$frostbiteLocation(T entity, CallbackInfoReturnable<Identifier> cir) {
		applyFrozenTextureIfApplicable(entity, cir);
	}

	@Inject(method = "getTextureResource*", at = @At("RETURN"), cancellable = true)
	private void enchancement$frostbiteResource(T entity, CallbackInfoReturnable<Identifier> cir) {
		applyFrozenTextureIfApplicable(entity, cir);
	}

	@Inject(method = "render*", at = @At("HEAD"), cancellable = true)
	private void enchancement$frostbite(T entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, CallbackInfo ci) {
		ModEntityComponents.FROZEN.maybeGet(entity).ifPresent(frozenComponent -> {
			if (frozenComponent.isFrozen()) {
				setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
				poseStack.push();
				dispatchedMat = poseStack.peek().getPositionMatrix().copy();
				EntityModelData entityModelData = new EntityModelData();
				entityModelData.isSitting = entity.hasVehicle();
				entityModelData.isChild = entity.isBaby();
				float bodyYaw = frozenComponent.getForcedBodyYaw();
				float pitch = frozenComponent.getForcedPitch();
				float headYawMinusBodyYaw = frozenComponent.getForcedHeadYaw() - bodyYaw;
				float limbDistance = Math.min(1, frozenComponent.getForcedLimbDistance());
				float limbAngleMinusLimbDistance = frozenComponent.getForcedLimbAngle() - frozenComponent.getForcedLimbDistance();
				float animationProgress = frozenComponent.getForcedClientAge() + partialTick;
				if (entity.isBaby()) {
					limbAngleMinusLimbDistance *= 3;
				}
				applyRotations(entity, poseStack, animationProgress, bodyYaw, partialTick);
				entityModelData.headPitch = -pitch;
				entityModelData.netHeadYaw = -headYawMinusBodyYaw;
				modelProvider.setLivingAnimations(entity, getInstanceId(entity), new AnimationEvent<T>(entity, limbAngleMinusLimbDistance, limbDistance, partialTick, limbDistance <= -getSwingMotionAnimThreshold() || limbDistance > getSwingMotionAnimThreshold(), Collections.singletonList(entityModelData)));
				poseStack.translate(0, 0.01F, 0);
				RenderSystem.setShaderTexture(0, getTextureLocation(entity));
				if (!entity.isInvisibleTo(MinecraftClient.getInstance().player)) {
					Color renderColor = getRenderColor(entity, partialTick, poseStack, bufferSource, null, packedLight);
					VertexConsumer glintBuffer = bufferSource.getBuffer(RenderLayer.getDirectEntityGlint());
					VertexConsumer translucentBuffer = bufferSource.getBuffer(RenderLayer.getEntityTranslucentCull(getTextureLocation(entity)));
					render(modelProvider.getModel(modelProvider.getModelResource(entity)), entity, partialTick, getRenderType(entity, partialTick, poseStack, bufferSource, null, packedLight, getTextureLocation(entity)), poseStack, bufferSource, glintBuffer != translucentBuffer ? VertexConsumers.union(glintBuffer, translucentBuffer) : null, packedLight, getOverlay(entity, 0), renderColor.getRed() / 255F, renderColor.getGreen() / 255F, renderColor.getBlue() / 255F, renderColor.getAlpha() / 255F);
				}
				if (!entity.isSpectator()) {
					for (GeoLayerRenderer<T> layerRenderer : layerRenderers) {
						renderLayer(poseStack, bufferSource, packedLight, entity, limbAngleMinusLimbDistance, limbDistance, partialTick, animationProgress, headYawMinusBodyYaw, pitch, bufferSource, layerRenderer);
					}
				}
				poseStack.pop();
				super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
				ci.cancel();
			}
		});
	}

	@Unique
	private void applyFrozenTextureIfApplicable(T entity, CallbackInfoReturnable<Identifier> cir) {
		if (ModEntityComponents.FROZEN.get(entity).isFrozen()) {
			cir.setReturnValue(FrozenReloadListener.INSTANCE.getTexture(cir.getReturnValue()));
		}
	}
}
