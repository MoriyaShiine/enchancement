/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.render.entity;

import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.client.render.entity.state.BrimstoneEntityRenderState;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class BrimstoneEntityRenderer extends ProjectileEntityRenderer<BrimstoneEntity, BrimstoneEntityRenderState> {
	private static final Identifier TEXTURE = Enchancement.id("textures/entity/projectiles/brimstone.png");

	public BrimstoneEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public BrimstoneEntityRenderState createRenderState() {
		return new BrimstoneEntityRenderState();
	}

	@Override
	protected Identifier getTexture(BrimstoneEntityRenderState state) {
		return TEXTURE;
	}

	@Override
	public void render(BrimstoneEntityRenderState renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState) {
		float scale = MathHelper.lerp(renderState.damage / 12F, 0.1F, 1);
		scale *= MathHelper.lerp(MathHelper.clamp((renderState.ticksExisted - (BrimstoneEntity.getMaxTicks() - 10F)) / (BrimstoneEntity.getMaxTicks() - (BrimstoneEntity.getMaxTicks() - 10)), 0, 1), 1F, 0);
		float v = (Math.floorMod(renderState.ticksExisted, 40) + renderState.age) / 4F;
		float u = v + 4 * -0.5F / scale;
		matrices.push();
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-renderState.yaw + 90));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(renderState.pitch + 90));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(renderState.ticksExisted + renderState.age * 12));
		matrices.scale(scale, 1, scale);
		matrices.translate(0, renderState.age, 0);
		for (int i = renderState.ticksExisted; i < renderState.distanceTraveled; i++) {
			renderSection(renderState, matrices, queue, u, v);
		}
		matrices.pop();
	}

	@Override
	public void updateRenderState(BrimstoneEntity entity, BrimstoneEntityRenderState state, float tickProgress) {
		super.updateRenderState(entity, state, tickProgress);
		state.distanceTraveled = entity.distanceTraveled;
		state.ticksExisted = entity.ticksExisted;
		state.damage = entity.getDamage();
		state.damageMultiplier = entity.getDamageMultiplier(entity.distanceTraveled - 1);
	}

	@Override
	protected boolean canBeCulled(BrimstoneEntity entity) {
		return false;
	}

	private static RenderLayer getRenderLayer() {
		if (EnchancementClient.irisLoaded && IrisApi.getInstance().isShaderPackInUse()) {
			return RenderLayer.getEntityCutoutNoCull(TEXTURE);
		}
		return RenderLayer.getEntityAlpha(TEXTURE);
	}

	private static void renderSection(BrimstoneEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, float u, float v) {
		matrices.scale(state.damageMultiplier, 1, state.damageMultiplier);
		for (int i = 0; i < 360; i += 15) {
			queue.submitCustom(matrices, getRenderLayer(), (entry, vertices) -> {
				drawPlane(entry, vertices, u, v);
			});
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i));
		}
		matrices.scale(1 / state.damageMultiplier, 1, 1 / state.damageMultiplier);
		matrices.translate(0, 1, 0);
	}

	private static void drawPlane(MatrixStack.Entry entry, VertexConsumer vertices, float u, float v) {
		drawVertex(entry, vertices, 0, 0.25F, 0, v);
		drawVertex(entry, vertices, 0, 0, 1, v);
		drawVertex(entry, vertices, 1, 0, 1, u);
		drawVertex(entry, vertices, 1, 0.25F, 0, u);
	}

	private static void drawVertex(MatrixStack.Entry entry, VertexConsumer vertices, float y, float z, float u, float v) {
		vertices.vertex(entry, 0.05F, y, z).color(Colors.WHITE).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0, -1, 0);
	}
}
