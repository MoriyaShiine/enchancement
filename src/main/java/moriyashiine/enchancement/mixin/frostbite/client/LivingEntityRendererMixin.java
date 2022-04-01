package moriyashiine.enchancement.mixin.frostbite.client;

import moriyashiine.enchancement.client.render.FrozenTextureManager;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
	@Shadow
	protected M model;

	@Shadow
	@Final
	protected List<FeatureRenderer<T, M>> features;

	protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Shadow
	protected abstract @Nullable
	RenderLayer getRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline);

	@Shadow
	protected abstract boolean isVisible(T entity);

	@Shadow
	protected abstract float getAnimationCounter(T entity, float tickDelta);

	@Shadow
	protected abstract float getAnimationProgress(T entity, float tickDelta);

	@Shadow
	protected abstract float getHandSwingProgress(T entity, float tickDelta);

	@Shadow
	protected abstract void scale(T entity, MatrixStack matrices, float amount);

	@Shadow
	protected abstract void setupTransforms(T entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta);

	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
	private void enchancement$frostbiteFreezeAnimations(T livingEntity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		ModEntityComponents.FROZEN.maybeGet(livingEntity).ifPresent(frozenComponent -> {
			if (frozenComponent.isFrozen()) {
				MinecraftClient client = MinecraftClient.getInstance();
				matrices.push();
				model.handSwingProgress = getHandSwingProgress(livingEntity, tickDelta);
				model.riding = livingEntity.hasVehicle();
				model.child = livingEntity.isBaby();
				float bodyYaw = frozenComponent.getForcedBodyYaw();
				float pitch = frozenComponent.getForcedPitch();
				float headYawMinusBodyYaw = frozenComponent.getForcedHeadYaw() - bodyYaw;
				float limbDistance = Math.min(1, frozenComponent.getForcedLimbDistance());
				float limbAngleMinusLimbDistance = frozenComponent.getForcedLimbAngle() - frozenComponent.getForcedLimbDistance();
				float animationProgress = getAnimationProgress(livingEntity, tickDelta);
				if (LivingEntityRenderer.shouldFlipUpsideDown(livingEntity)) {
					pitch *= -1;
					headYawMinusBodyYaw *= -1;
				}
				if (livingEntity.isBaby()) {
					limbAngleMinusLimbDistance *= 3;
				}
				setupTransforms(livingEntity, matrices, animationProgress, bodyYaw, tickDelta);
				matrices.scale(-1, -1, 1);
				scale(livingEntity, matrices, tickDelta);
				matrices.translate(0, -1.501F, 0);
				model.animateModel(livingEntity, limbAngleMinusLimbDistance, limbDistance, tickDelta);
				model.setAngles(livingEntity, limbAngleMinusLimbDistance, limbDistance, 0, headYawMinusBodyYaw, pitch);
				boolean visible = isVisible(livingEntity);
				boolean translucent = !visible && !livingEntity.isInvisibleTo(client.player);
				RenderLayer renderLayer = getRenderLayer(livingEntity, visible, translucent, client.hasOutline(livingEntity));
				if (renderLayer != null) {
					model.render(matrices, vertexConsumers.getBuffer(renderLayer), light, LivingEntityRenderer.getOverlay(livingEntity, getAnimationCounter(livingEntity, tickDelta)), 1, 1, 1, translucent ? 0.15F : 1);
				}
				if (!livingEntity.isSpectator()) {
					for (FeatureRenderer<T, M> featureRenderer : features) {
						featureRenderer.render(matrices, vertexConsumers, light, livingEntity, limbAngleMinusLimbDistance, limbDistance, tickDelta, animationProgress, headYawMinusBodyYaw, pitch);
					}
				}
				matrices.pop();
				super.render(livingEntity, yaw, tickDelta, matrices, vertexConsumers, light);
				ci.cancel();
			}
		});
	}

	@Inject(method = "isShaking", at = @At("HEAD"), cancellable = true)
	private void enchancement$frostbiteStopShaking(T entity, CallbackInfoReturnable<Boolean> cir) {
		if (entity instanceof MobEntity && ModEntityComponents.FROZEN.get(entity).isFrozen()) {
			cir.setReturnValue(false);
		}
	}

	@ModifyVariable(method = "getRenderLayer", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;"))
	private Identifier enchancement$frostbiteTexture(Identifier value, LivingEntity living) {
		if (living instanceof MobEntity && ModEntityComponents.FROZEN.get(living).isFrozen()) {
			return FrozenTextureManager.getInstance().getTexture(value);
		}
		return value;
	}
}
