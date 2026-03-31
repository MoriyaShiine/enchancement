/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import moriyashiine.enchancement.client.renderer.entity.state.BrimstoneEntityRenderState;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.Brimstone;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;

import java.util.function.Function;

public class BrimstoneRenderer extends ArrowRenderer<Brimstone, BrimstoneEntityRenderState> {
	private static final Function<Identifier, RenderType> BRIMSTONE_TYPE = Util.memoize(
			texture -> {
				RenderSetup state = RenderSetup.builder(RenderPipelines.OPAQUE_PARTICLE)
						.withTexture("Sampler0", texture)
						.useLightmap()
						.useOverlay()
						.affectsCrumbling()
						.setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
						.createRenderSetup();
				return RenderType.create("entity_cutout_cull", state);
			}
	);

	private static final Identifier TEXTURE = Enchancement.id("textures/entity/projectiles/brimstone.png");

	public BrimstoneRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public BrimstoneEntityRenderState createRenderState() {
		return new BrimstoneEntityRenderState();
	}

	@Override
	protected Identifier getTextureLocation(BrimstoneEntityRenderState state) {
		return TEXTURE;
	}

	@Override
	public void submit(BrimstoneEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		float scale = Mth.lerp(state.damage / 12F, 0.1F, 1);
		scale *= Mth.lerp(Mth.clamp((state.ticksExisted - (Brimstone.getMaxTicks() - 10F)) / (Brimstone.getMaxTicks() - (Brimstone.getMaxTicks() - 10)), 0, 1), 1F, 0);
		float v = (Math.floorMod(state.ticksExisted, 40) + state.ageInTicks) / 4F;
		float u = v + 4 * -0.5F / scale;
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(-state.yRot + 90));
		poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot + 90));
		poseStack.mulPose(Axis.YP.rotationDegrees(state.ticksExisted + state.ageInTicks * 12));
		poseStack.scale(scale, 1, scale);
		poseStack.translate(0, state.ageInTicks, 0);
		for (int i = state.ticksExisted; i < state.distanceTraveled; i++) {
			extractSection(state, poseStack, submitNodeCollector, u, v);
		}
		poseStack.popPose();
	}

	@Override
	public void extractRenderState(Brimstone entity, BrimstoneEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.distanceTraveled = entity.distanceTraveled;
		state.ticksExisted = entity.ticksExisted;
		state.damage = entity.getDamage();
		state.damageMultiplier = entity.getDamageMultiplier(entity.distanceTraveled - 1);
	}

	@Override
	protected boolean affectedByCulling(Brimstone entity) {
		return false;
	}

	private static void extractSection(BrimstoneEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, float u, float v) {
		poseStack.scale(state.damageMultiplier, 1, state.damageMultiplier);
		for (int i = 0; i < 360; i += 15) {
			submitNodeCollector.submitCustomGeometry(poseStack, BRIMSTONE_TYPE.apply(TEXTURE), (pose, consumer) -> extractPlane(pose, consumer, u, v));
			poseStack.mulPose(Axis.YP.rotationDegrees(i));
		}
		poseStack.scale(1 / state.damageMultiplier, 1, 1 / state.damageMultiplier);
		poseStack.translate(0, 1, 0);
	}

	private static void extractPlane(PoseStack.Pose pose, VertexConsumer consumer, float u, float v) {
		extractVertex(pose, consumer, 0, 0.25F, 0, v);
		extractVertex(pose, consumer, 0, 0, 1, v);
		extractVertex(pose, consumer, 1, 0, 1, u);
		extractVertex(pose, consumer, 1, 0.25F, 0, u);

		extractVertex(pose, consumer, 1, 0.25F, 0, u);
		extractVertex(pose, consumer, 1, 0, 1, u);
		extractVertex(pose, consumer, 0, 0, 1, v);
		extractVertex(pose, consumer, 0, 0.25F, 0, v);
	}

	private static void extractVertex(PoseStack.Pose pose, VertexConsumer consumer, float y, float z, float u, float v) {
		consumer.addVertex(pose, 0.05F, y, z).setColor(CommonColors.WHITE).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightCoordsUtil.FULL_BRIGHT).setNormal(pose, 0, 0, 0);
	}
}
