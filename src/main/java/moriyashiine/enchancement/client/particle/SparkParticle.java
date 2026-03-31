/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import moriyashiine.enchancement.client.particle.group.SparkParticleGroup;
import moriyashiine.enchancement.common.particle.SparkParticleOption;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SparkParticle extends Particle {
	private final Vec3 destination;
	private final List<Vec3> arcs = new ArrayList<>();

	protected SparkParticle(ClientLevel level, double x, double y, double z, Vec3 destination) {
		super(level, x, y, z);
		this.destination = destination;
		lifetime = 5;
		randomizeArcs();
	}

	@Override
	public ParticleRenderType getGroup() {
		return SparkParticleGroup.SHEET;
	}

	@Override
	public void tick() {
		super.tick();
		if (level.getRandom().nextInt(5) == 0) {
			randomizeArcs();
		}
	}

	public SparkParticleGroup.State createState(Camera camera) {
		Vec3 pos = camera.position();
		return new SparkParticleGroup.State(new PoseStack(), arcs.stream().map(arc -> new Vector3f((float) (arc.x() - pos.x()), (float) (arc.y() - pos.y()), (float) (arc.z() - pos.z()))).toList());
	}

	private void randomizeArcs() {
		arcs.clear();
		double length = Math.sqrt(destination.distanceToSqr(x, y, z));
		double arcCount = length / 0.5F;
		Vec3 normal = destination.subtract(x, y, z).normalize();
		for (int i = 0; i < arcCount; i++) {
			arcs.add(normal.scale(length * (i / arcCount)).add(x, y, z).offsetRandom(level.getRandom(), 0.5F));
		}
		arcs.add(destination);
	}

	public static class Provider implements ParticleProvider<SparkParticleOption> {
		@Override
		public @Nullable Particle createParticle(SparkParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
			return new SparkParticle(level, x, y, z, options.destination());
		}
	}
}
