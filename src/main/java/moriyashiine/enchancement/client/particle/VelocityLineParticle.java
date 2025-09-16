/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;

public class VelocityLineParticle extends SpriteBillboardParticle {
	private final boolean vertical;
	private final float yaw;

	public VelocityLineParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(world, x, y, z);
		vertical = velocityY != 0;
		if (vertical) {
			angle = MathHelper.HALF_PI;
			lastAngle = angle;
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
	public void render(VertexConsumer vertexConsumer, Camera camera, float tickProgress) {
		if (vertical) {
			super.render(vertexConsumer, camera, tickProgress);
		} else {
			Quaternionf quaternionf = new Quaternionf();
			quaternionf.rotationYXZ(yaw, 0, 0);
			render(vertexConsumer, camera, quaternionf, tickProgress);
			quaternionf.rotationYXZ(yaw, MathHelper.PI, 0);
			render(vertexConsumer, camera, quaternionf, tickProgress);
		}
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public Rotator getRotator() {
		return Rotator.Y_AND_W_ONLY;
	}

	public record Factory(SpriteProvider spriteProvider) implements ParticleFactory<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			VelocityLineParticle particle = new VelocityLineParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setSpriteForAge(spriteProvider());
			return particle;
		}
	}
}
