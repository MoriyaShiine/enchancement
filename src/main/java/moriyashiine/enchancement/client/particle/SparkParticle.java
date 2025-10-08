/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.particle;

import moriyashiine.enchancement.client.particle.render.SparkParticleRenderer;
import moriyashiine.enchancement.common.particle.SparkParticleEffect;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
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
	}

	@Override
	public ParticleTextureSheet textureSheet() {
		return SparkParticleRenderer.SHEET;
	}

	@Override
	public void tick() {
		super.tick();
		if (world.getRandom().nextInt(5) == 0) {
			randomizeArcs();
		}
	}

	public SparkParticleRenderer.State createState(Camera camera) {
		Vec3d pos = camera.getPos();
		return new SparkParticleRenderer.State(new MatrixStack(), arcs.stream().map(arc -> new Vector3f((float) (arc.getX() - pos.getX()), (float) (arc.getY() - pos.getY()), (float) (arc.getZ() - pos.getZ()))).toList());
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
		public @Nullable Particle createParticle(SparkParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
			return new SparkParticle(world, x, y, z, parameters.destination());
		}
	}
}
