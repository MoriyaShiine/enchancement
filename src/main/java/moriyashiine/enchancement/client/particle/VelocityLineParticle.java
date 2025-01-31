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
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class VelocityLineParticle extends SpriteBillboardParticle {
	private final boolean vertical;
	private final float yaw;

	public VelocityLineParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(world, x, y, z);
		vertical = velocityY != 0;
		if (vertical) {
			angle = MathHelper.HALF_PI;
			prevAngle = angle;
		}
		yaw = (float) MathHelper.atan2(velocityX, velocityZ) + MathHelper.HALF_PI;
		maxAge = 3;
		red = MathHelper.nextFloat(world.getRandom(), 0.8F, 1);
		green = MathHelper.nextFloat(world.getRandom(), 0.8F, 1);
		blue = 0.5F;
		alpha = MathHelper.nextFloat(world.getRandom(), 0.6F, 0.8F);
	}

	@Override
	public void tick() {
		alpha -= 0.2F;
		if (age++ >= maxAge) {
			markDead();
		}
	}

	@Override
	public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		if (vertical) {
			super.render(vertexConsumer, camera, tickDelta);
		} else {
			Quaternionf quaternionf = new Quaternionf();
			quaternionf.rotationYXZ(yaw, 0, 0);
			this.method_60373(vertexConsumer, camera, quaternionf, tickDelta);
			quaternionf.rotationYXZ(yaw, MathHelper.PI, 0);
			this.method_60373(vertexConsumer, camera, quaternionf, tickDelta);
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

	public static class Factory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			VelocityLineParticle particle = new VelocityLineParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setSpriteForAge(spriteProvider);
			return particle;
		}
	}
}
