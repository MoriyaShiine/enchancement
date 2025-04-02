/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.particle;

import moriyashiine.enchancement.common.particle.SparkParticleEffect;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class SparkParticle extends Particle {
	private final Vec3d destination;
	private final List<Vec3d> arcs = new ArrayList<>();

	protected SparkParticle(ClientWorld world, double x, double y, double z, Vec3d destination) {
		super(world, x, y, z);
		this.destination = destination;
		maxAge = 5;
		randomizeArcs();
		setColor(0.2F, 0.7F, 0.3F);
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.CUSTOM;
	}

	@Override
	public void tick() {
		super.tick();
		if (world.getRandom().nextInt(5) == 0) {
			randomizeArcs();
		}
	}

	@Override
	public void render(VertexConsumer vertexConsumer, Camera camera, float tickProgress) {
	}

	@Override
	public void renderCustom(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Camera camera, float tickProgress) {
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLightning());
		Vec3d cameraPos = camera.getPos();
		for (int i = 1; i < arcs.size(); i++) {
			Vec3d start = arcs.get(i - 1), end = arcs.get(i);
			float startX = (float) (start.getX() - cameraPos.getX());
			float startY = (float) (start.getY() - cameraPos.getY());
			float startZ = (float) (start.getZ() - cameraPos.getZ());
			float endX = (float) (end.getX() - cameraPos.getX());
			float endY = (float) (end.getY() - cameraPos.getY());
			float endZ = (float) (end.getZ() - cameraPos.getZ());
			Vector3f normal = new Vector3f(endX - startX, endY - startY, endZ - startZ).normalize();
			Vector3f verticalOffset = normal.cross(normal.x(), normal.y(), 0, new Vector3f()).normalize().mul(0.025F);
			Vector3f horizontalOffset = normal.cross(normal.x(), 0, normal.z(), new Vector3f()).normalize().mul(0.025F);
			Matrix4f position = matrices.peek().getPositionMatrix();
			drawFace(vertexConsumer, position,
					startX + verticalOffset.x(), startY + verticalOffset.y(), startZ + verticalOffset.z(),
					endX + verticalOffset.x(), endY + verticalOffset.y(), endZ + verticalOffset.z(),
					endX + horizontalOffset.x(), endY + horizontalOffset.y(), endZ + horizontalOffset.z(),
					startX + horizontalOffset.x(), startY + horizontalOffset.y(), startZ + horizontalOffset.z());
			drawFace(vertexConsumer, position,
					startX + horizontalOffset.x(), startY + horizontalOffset.y(), startZ + horizontalOffset.z(),
					endX + horizontalOffset.x(), endY + horizontalOffset.y(), endZ + horizontalOffset.z(),
					endX - verticalOffset.x(), endY - verticalOffset.y(), endZ - verticalOffset.z(),
					startX - verticalOffset.x(), startY - verticalOffset.y(), startZ - verticalOffset.z());
			drawFace(vertexConsumer, position,
					startX - verticalOffset.x(), startY - verticalOffset.y(), startZ - verticalOffset.z(),
					endX - verticalOffset.x(), endY - verticalOffset.y(), endZ - verticalOffset.z(),
					endX - horizontalOffset.x(), endY - horizontalOffset.y(), endZ - horizontalOffset.z(),
					startX - horizontalOffset.x(), startY - horizontalOffset.y(), startZ - horizontalOffset.z());
			drawFace(vertexConsumer, position,
					startX - horizontalOffset.x(), startY - horizontalOffset.y(), startZ - horizontalOffset.z(),
					endX - horizontalOffset.x(), endY - horizontalOffset.y(), endZ - horizontalOffset.z(),
					endX + verticalOffset.x(), endY + verticalOffset.y(), endZ + verticalOffset.z(),
					startX + verticalOffset.x(), startY + verticalOffset.y(), startZ + verticalOffset.z());
		}
	}

	private void drawFace(VertexConsumer consumer, Matrix4f matrix4f, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
		consumer.vertex(matrix4f, x1, y1, z1).color(red, green, blue, alpha);
		consumer.vertex(matrix4f, x2, y2, z2).color(red, green, blue, alpha);
		consumer.vertex(matrix4f, x3, y3, z3).color(red, green, blue, alpha);
		consumer.vertex(matrix4f, x4, y4, z4).color(red, green, blue, alpha);

		consumer.vertex(matrix4f, x1, y1, z1).color(red, green, blue, alpha);
		consumer.vertex(matrix4f, x4, y4, z4).color(red, green, blue, alpha);
		consumer.vertex(matrix4f, x3, y3, z3).color(red, green, blue, alpha);
		consumer.vertex(matrix4f, x2, y2, z2).color(red, green, blue, alpha);
	}

	private void randomizeArcs() {
		arcs.clear();
		double length = Math.sqrt(destination.squaredDistanceTo(x, y, z));
		double arcCount = length / 0.5F;
		Vec3d normal = destination.subtract(x, y, z).normalize();
		for (int i = 0; i < arcCount; i++) {
			arcs.add(normal.multiply(length * (i / arcCount)).add(x, y, z).addRandom(world.getRandom(), 0.5F));
		}
		arcs.add(destination);
	}

	public static class Factory implements ParticleFactory<SparkParticleEffect> {
		@Override
		public @Nullable Particle createParticle(SparkParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new SparkParticle(world, x, y, z, parameters.destination());
		}
	}
}
