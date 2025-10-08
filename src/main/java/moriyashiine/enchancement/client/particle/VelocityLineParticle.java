/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.joml.Quaternionf;

public class VelocityLineParticle extends BillboardParticle {
	private final boolean vertical;
	private final float yaw;

	public VelocityLineParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, spriteProvider.getFirst());
		vertical = velocityY != 0;
		if (vertical) {
			zRotation = lastZRotation = MathHelper.HALF_PI;
		}
		yaw = (float) MathHelper.atan2(velocityX, velocityZ) + MathHelper.HALF_PI;
		maxAge = 3;
		setColor(MathHelper.nextFloat(random, 0.8F, 1), MathHelper.nextFloat(random, 0.8F, 1), 0.5F);
		alpha = MathHelper.nextFloat(random, 0.6F, 0.8F);
	}

	@Override
	public void tick() {
		alpha -= 0.2F;
		if (age++ >= maxAge) {
			markDead();
		}
	}

	@Override
	public void render(BillboardParticleSubmittable submittable, Camera camera, float tickProgress) {
		if (vertical) {
			super.render(submittable, camera, tickProgress);
		} else {
			Quaternionf quaternionf = new Quaternionf();
			quaternionf.rotationYXZ(yaw, 0, 0);
			render(submittable, camera, quaternionf, tickProgress);
			quaternionf.rotationYXZ(yaw, MathHelper.PI, 0);
			render(submittable, camera, quaternionf, tickProgress);
		}
	}

	@Override
	protected RenderType getRenderType() {
		return RenderType.PARTICLE_ATLAS_TRANSLUCENT;
	}

	@Override
	public Rotator getRotator() {
		return Rotator.Y_AND_W_ONLY;
	}

	public record Factory(SpriteProvider spriteProvider) implements ParticleFactory<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
			return new VelocityLineParticle(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider());
		}
	}
}
