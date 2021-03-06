/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.client.render.entity;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class BrimstoneEntityRenderer extends ProjectileEntityRenderer<BrimstoneEntity> {
	private static final Identifier TEXTURE = new Identifier(Enchancement.MOD_ID, "textures/entity/brimstone.png");

	public BrimstoneEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public Identifier getTexture(BrimstoneEntity entity) {
		return TEXTURE;
	}

	@Override
	public void render(BrimstoneEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		float scale = MathHelper.lerp(entity.ticksExisted / 20F, 1, 0.0625F);
		float v = (Math.floorMod(entity.world.getTime(), 40) + tickDelta) / 4;
		float u = v + 4 * -0.5F / scale;
		VertexConsumer vertices = vertexConsumers.getBuffer(RenderLayer.getEntityAlpha(TEXTURE));
		matrices.push();
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) + 90));
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch()) + 90));
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((entity.world.getTime() + entity.age) * 12));
		matrices.scale(scale, 1, scale);
		MatrixStack.Entry entry = matrices.peek();
		for (int j = 0; j < entity.maxY; j++) {
			for (int i = 0; i < 360; i += 15) {
				drawPlane(entry.getPositionMatrix(), entry.getNormalMatrix(), vertices, u, v);
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(i));
			}
			matrices.translate(0, 1, 0);
		}
		matrices.pop();
	}

	private static void drawPlane(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertices, float u, float v) {
		drawVertex(positionMatrix, normalMatrix, vertices, 1, 0, 1, u);
		drawVertex(positionMatrix, normalMatrix, vertices, 0, 0, 1, v);
		drawVertex(positionMatrix, normalMatrix, vertices, 0, 0.25F, 0, v);
		drawVertex(positionMatrix, normalMatrix, vertices, 1, 0.25F, 0, u);

		drawVertex(positionMatrix, normalMatrix, vertices, 0, 0.25F, 0, v);
		drawVertex(positionMatrix, normalMatrix, vertices, 0, 0, 1, v);
		drawVertex(positionMatrix, normalMatrix, vertices, 1, 0, 1, u);
		drawVertex(positionMatrix, normalMatrix, vertices, 1, 0.25F, 0, u);
	}

	private static void drawVertex(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertices, int y, float z, float u, float v) {
		vertices.vertex(positionMatrix, 0, y, z).color(255, 255, 255, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(normalMatrix, 0, 1, 0).next();
	}
}
