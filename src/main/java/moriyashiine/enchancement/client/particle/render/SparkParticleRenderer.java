/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.particle.render;

import moriyashiine.enchancement.client.particle.SparkParticle;
import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleRenderer;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.*;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector3f;

import java.util.List;

public class SparkParticleRenderer extends ParticleRenderer<SparkParticle> {
	public static final ParticleTextureSheet SHEET = new ParticleTextureSheet(Enchancement.id("spark").toString());

	public SparkParticleRenderer(ParticleManager particleManager) {
		super(particleManager);
	}

	@Override
	public Submittable render(Frustum frustum, Camera camera, float tickProgress) {
		return new Result(getParticles().stream().map(particle -> particle.createState(camera)).toList());
	}

	public record State(MatrixStack matrices, List<Vector3f> arcs) {
	}

	public record Result(List<State> states) implements Submittable {
		@Override
		public void submit(OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState) {
			for (State state : states()) {
				for (int i = 1; i < state.arcs().size(); i++) {
					Vector3f start = state.arcs().get(i - 1), end = state.arcs().get(i);
					Vector3f normal = new Vector3f(end.x() - start.x(), end.y() - start.y(), end.z() - start.z()).normalize();
					Vector3f verticalOffset = normal.cross(normal.x(), normal.y(), 0, new Vector3f()).normalize().mul(0.025F);
					Vector3f horizontalOffset = normal.cross(normal.x(), 0, normal.z(), new Vector3f()).normalize().mul(0.025F);
					queue.submitCustom(state.matrices(), RenderLayer.getLightning(), (entry, vertices) -> {
						drawFace(vertices, entry,
								start.x() + verticalOffset.x(), start.y() + verticalOffset.y(), start.z() + verticalOffset.z(),
								end.x() + verticalOffset.x(), end.y() + verticalOffset.y(), end.z() + verticalOffset.z(),
								end.x() + horizontalOffset.x(), end.y() + horizontalOffset.y(), end.z() + horizontalOffset.z(),
								start.x() + horizontalOffset.x(), start.y() + horizontalOffset.y(), start.z() + horizontalOffset.z());
						drawFace(vertices, entry,
								start.x() + horizontalOffset.x(), start.y() + horizontalOffset.y(), start.z() + horizontalOffset.z(),
								end.x() + horizontalOffset.x(), end.y() + horizontalOffset.y(), end.z() + horizontalOffset.z(),
								end.x() - verticalOffset.x(), end.y() - verticalOffset.y(), end.z() - verticalOffset.z(),
								start.x() - verticalOffset.x(), start.y() - verticalOffset.y(), start.z() - verticalOffset.z());
						drawFace(vertices, entry,
								start.x() - verticalOffset.x(), start.y() - verticalOffset.y(), start.z() - verticalOffset.z(),
								end.x() - verticalOffset.x(), end.y() - verticalOffset.y(), end.z() - verticalOffset.z(),
								end.x() - horizontalOffset.x(), end.y() - horizontalOffset.y(), end.z() - horizontalOffset.z(),
								start.x() - horizontalOffset.x(), start.y() - horizontalOffset.y(), start.z() - horizontalOffset.z());
						drawFace(vertices, entry,
								start.x() - horizontalOffset.x(), start.y() - horizontalOffset.y(), start.z() - horizontalOffset.z(),
								end.x() - horizontalOffset.x(), end.y() - horizontalOffset.y(), end.z() - horizontalOffset.z(),
								end.x() + verticalOffset.x(), end.y() + verticalOffset.y(), end.z() + verticalOffset.z(),
								start.x() + verticalOffset.x(), start.y() + verticalOffset.y(), start.z() + verticalOffset.z());
					});
				}
			}
		}

		private void drawFace(VertexConsumer consumer, MatrixStack.Entry entry, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
			consumer.vertex(entry.getPositionMatrix(), x1, y1, z1).color(0.2F, 0.7F, 0.3F, 1);
			consumer.vertex(entry.getPositionMatrix(), x2, y2, z2).color(0.2F, 0.7F, 0.3F, 1);
			consumer.vertex(entry.getPositionMatrix(), x3, y3, z3).color(0.2F, 0.7F, 0.3F, 1);
			consumer.vertex(entry.getPositionMatrix(), x4, y4, z4).color(0.2F, 0.7F, 0.3F, 1);

			consumer.vertex(entry.getPositionMatrix(), x1, y1, z1).color(0.2F, 0.7F, 0.3F, 1);
			consumer.vertex(entry.getPositionMatrix(), x4, y4, z4).color(0.2F, 0.7F, 0.3F, 1);
			consumer.vertex(entry.getPositionMatrix(), x3, y3, z3).color(0.2F, 0.7F, 0.3F, 1);
			consumer.vertex(entry.getPositionMatrix(), x2, y2, z2).color(0.2F, 0.7F, 0.3F, 1);
		}
	}
}
