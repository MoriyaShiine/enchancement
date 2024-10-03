/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.render.entity;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class BrimstoneEntityRenderer extends ProjectileEntityRenderer<BrimstoneEntity> {
	private static final Identifier TEXTURE = Enchancement.id("textures/entity/brimstone.png");

	public BrimstoneEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public Identifier getTexture(BrimstoneEntity entity) {
		return TEXTURE;
	}

	@Override
	public void render(BrimstoneEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		float scale = MathHelper.lerp((float) entity.getDamage() / 12F, 0.1F, 1);
		scale *= MathHelper.lerp(MathHelper.clamp((entity.ticksExisted - 32F) / (BrimstoneEntity.MAX_TICKS - 32), 0, 1), 1F, 0);
		float v = (Math.floorMod(entity.getWorld().getTime(), 40) + tickDelta) / 4;
		float u = v + 4 * -0.5F / scale;
		VertexConsumer vertices = vertexConsumers.getBuffer(RenderLayer.getEntityAlpha(TEXTURE));
		matrices.push();
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) + 90));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch()) + 90));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.getWorld().getTime() + entity.ticksExisted + tickDelta) * 12));
		matrices.scale(scale, 1, scale);
		MatrixStack.Entry entry = matrices.peek();
		matrices.translate(0, entity.ticksExisted + tickDelta, 0);
		for (int j = entity.ticksExisted + 1; j < entity.distanceTraveled; j++) {
			renderSection(entity, matrices, entry, vertices, u, v);
		}
		matrices.pop();
	}

	private static void renderSection(BrimstoneEntity entity, MatrixStack matrices, MatrixStack.Entry entry, VertexConsumer vertices, float u, float v) {
		float multiplier = entity.getDamageMultiplier(entity.distanceTraveled - 1);
		matrices.scale(multiplier, 1, multiplier);
		for (int i = 0; i < 360; i += 15) {
			drawPlane(entry, vertices, u, v);
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i));
		}
		matrices.scale(1 / multiplier, 1, 1 / multiplier);
		matrices.translate(0, 1, 0);
	}

	private static void drawPlane(MatrixStack.Entry entry, VertexConsumer vertices, float u, float v) {
		drawVertex(entry, vertices, 0, 0.25F, 0, v);
		drawVertex(entry, vertices, 0, 0, 1, v);
		drawVertex(entry, vertices, 1, 0, 1, u);
		drawVertex(entry, vertices, 1, 0.25F, 0, u);
	}

	private static void drawVertex(MatrixStack.Entry entry, VertexConsumer vertices, float y, float z, float u, float v) {
		vertices.vertex(entry.getPositionMatrix(), 0, y, z).color(255, 255, 255, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0, 1, 0);
	}
}
