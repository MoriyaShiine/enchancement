/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.frostbite.client.integration.geckolib;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.enchancement.client.reloadlisteners.FrozenReloadListener;
import moriyashiine.enchancement.common.component.entity.FrozenComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

@Mixin(value = GeoEntityRenderer.class, remap = false)
public abstract class GeoEntityRendererMixin<T extends Entity & GeoAnimatable> extends EntityRenderer<T> implements GeoRenderer<T> {
	@Shadow
	@Final
	protected GeoModel<T> model;

	@Shadow
	protected Matrix4f modelRenderTranslations;

	@Shadow
	public abstract Identifier getTexture(T animatable);

	@Shadow
	protected abstract void applyRotations(T animatable, MatrixStack poseStack, float ageInTicks, float rotationYaw, float partialTick);

	@Shadow
	public abstract void renderRecursively(MatrixStack poseStack, T animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha);

	protected GeoEntityRendererMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Override
	public RenderLayer getRenderType(T animatable, Identifier texture, @Nullable VertexConsumerProvider bufferSource, float partialTick) {
		if (animatable instanceof LivingEntity && ModEntityComponents.FROZEN.get(animatable).isFrozen()) {
			return RenderLayer.getEntitySolid(FrozenReloadListener.INSTANCE.getTexture(texture));
		}
		return getGeoModel().getRenderType(animatable, texture);
	}

	@Inject(method = "actuallyRender*", at = @At("HEAD"), cancellable = true)
	private void enchancement$frostbite(MatrixStack poseStack, T animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, CallbackInfo ci) {
		if (animatable instanceof LivingEntity entity) {
			FrozenComponent frozenComponent = ModEntityComponents.FROZEN.get(entity);
			if (frozenComponent.isFrozen()) {
				poseStack.push();
				float lerpBodyRot = frozenComponent.getForcedBodyYaw();
				float limbAngle = frozenComponent.getForcedLimbAngle();
				applyRotations(animatable, poseStack, frozenComponent.getForcedClientAge() + partialTick, lerpBodyRot, partialTick);
				if (entity.isBaby()) {
					limbAngle *= 3;
				}
				if (!isReRender) {
					AnimationState<T> animationState = new AnimationState<>(animatable, limbAngle, frozenComponent.getForcedLimbDistance(), partialTick, false);
					long instanceId = getInstanceId(animatable);
					animationState.setData(DataTickets.TICK, animatable.getTick(animatable));
					animationState.setData(DataTickets.ENTITY, animatable);
					animationState.setData(DataTickets.ENTITY_MODEL_DATA, new EntityModelData(animatable.hasVehicle(), entity.isBaby(), -(frozenComponent.getForcedHeadYaw() - lerpBodyRot), -frozenComponent.getForcedPitch()));
					this.model.addAdditionalStateData(animatable, instanceId, animationState::setData);
					this.model.handleAnimations(animatable, instanceId, animationState);
				}
				poseStack.translate(0, 0.01F, 0);
				RenderSystem.setShaderTexture(0, FrozenReloadListener.INSTANCE.getTexture(getTexture(animatable)));
				modelRenderTranslations = new Matrix4f(poseStack.peek().getPositionMatrix());
				if (!animatable.isInvisibleTo(MinecraftClient.getInstance().player)) {
					for (GeoBone group : model.topLevelBones()) {
						renderRecursively(poseStack, animatable, group, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
					}
				}
				poseStack.pop();
				ci.cancel();
			}
		}
	}
}
