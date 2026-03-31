/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.particle.group;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import moriyashiine.enchancement.client.particle.SparkParticle;
import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
import org.joml.Vector3f;

import java.util.List;

public class SparkParticleGroup extends ParticleGroup<SparkParticle> {
	public static final ParticleRenderType SHEET = new ParticleRenderType(Enchancement.id("spark").toString());

	public SparkParticleGroup(ParticleEngine engine) {
		super(engine);
	}

	@Override
	public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTickTime) {
		return new Result(getAll().stream().map(particle -> particle.createState(camera)).toList());
	}

	public record State(PoseStack poseStack, List<Vector3f> arcs) {
	}

	public record Result(List<State> states) implements ParticleGroupRenderState {
		@Override
		public void submit(SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
			for (State state : states()) {
				for (int i = 1; i < state.arcs().size(); i++) {
					Vector3f start = state.arcs().get(i - 1), end = state.arcs().get(i);
					Vector3f normal = new Vector3f(end.x() - start.x(), end.y() - start.y(), end.z() - start.z()).normalize();
					Vector3f verticalOffset = normal.cross(normal.x(), normal.y(), 0, new Vector3f()).normalize().mul(0.025F);
					Vector3f horizontalOffset = normal.cross(normal.x(), 0, normal.z(), new Vector3f()).normalize().mul(0.025F);
					submitNodeCollector.submitCustomGeometry(state.poseStack(), RenderTypes.lightning(), (pose, consumer) -> {
						drawFace(consumer, pose,
								start.x() + verticalOffset.x(), start.y() + verticalOffset.y(), start.z() + verticalOffset.z(),
								end.x() + verticalOffset.x(), end.y() + verticalOffset.y(), end.z() + verticalOffset.z(),
								end.x() + horizontalOffset.x(), end.y() + horizontalOffset.y(), end.z() + horizontalOffset.z(),
								start.x() + horizontalOffset.x(), start.y() + horizontalOffset.y(), start.z() + horizontalOffset.z());
						drawFace(consumer, pose,
								start.x() + horizontalOffset.x(), start.y() + horizontalOffset.y(), start.z() + horizontalOffset.z(),
								end.x() + horizontalOffset.x(), end.y() + horizontalOffset.y(), end.z() + horizontalOffset.z(),
								end.x() - verticalOffset.x(), end.y() - verticalOffset.y(), end.z() - verticalOffset.z(),
								start.x() - verticalOffset.x(), start.y() - verticalOffset.y(), start.z() - verticalOffset.z());
						drawFace(consumer, pose,
								start.x() - verticalOffset.x(), start.y() - verticalOffset.y(), start.z() - verticalOffset.z(),
								end.x() - verticalOffset.x(), end.y() - verticalOffset.y(), end.z() - verticalOffset.z(),
								end.x() - horizontalOffset.x(), end.y() - horizontalOffset.y(), end.z() - horizontalOffset.z(),
								start.x() - horizontalOffset.x(), start.y() - horizontalOffset.y(), start.z() - horizontalOffset.z());
						drawFace(consumer, pose,
								start.x() - horizontalOffset.x(), start.y() - horizontalOffset.y(), start.z() - horizontalOffset.z(),
								end.x() - horizontalOffset.x(), end.y() - horizontalOffset.y(), end.z() - horizontalOffset.z(),
								end.x() + verticalOffset.x(), end.y() + verticalOffset.y(), end.z() + verticalOffset.z(),
								start.x() + verticalOffset.x(), start.y() + verticalOffset.y(), start.z() + verticalOffset.z());
					});
				}
			}
		}

		private void drawFace(VertexConsumer consumer, PoseStack.Pose pose, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
			consumer.addVertex(pose.pose(), x1, y1, z1).setColor(0.2F, 0.7F, 0.3F, 1);
			consumer.addVertex(pose.pose(), x2, y2, z2).setColor(0.2F, 0.7F, 0.3F, 1);
			consumer.addVertex(pose.pose(), x3, y3, z3).setColor(0.2F, 0.7F, 0.3F, 1);
			consumer.addVertex(pose.pose(), x4, y4, z4).setColor(0.2F, 0.7F, 0.3F, 1);

			consumer.addVertex(pose.pose(), x1, y1, z1).setColor(0.2F, 0.7F, 0.3F, 1);
			consumer.addVertex(pose.pose(), x4, y4, z4).setColor(0.2F, 0.7F, 0.3F, 1);
			consumer.addVertex(pose.pose(), x3, y3, z3).setColor(0.2F, 0.7F, 0.3F, 1);
			consumer.addVertex(pose.pose(), x2, y2, z2).setColor(0.2F, 0.7F, 0.3F, 1);
		}
	}
}
