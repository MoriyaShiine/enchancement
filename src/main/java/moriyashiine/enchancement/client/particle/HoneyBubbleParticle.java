/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.particle;

import moriyashiine.enchancement.common.particle.HoneyBubbleParticleEffect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class HoneyBubbleParticle extends SpriteBillboardParticle {
	private final SpriteProvider spriteProvider;

	public HoneyBubbleParticle(UUID ownerId, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z);
		this.spriteProvider = spriteProvider;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;
		setSpriteForAge(spriteProvider);
		maxAge = 3;
		gravityStrength = 0.008F;
		if (MinecraftClient.getInstance().getCameraEntity().getUuid().equals(ownerId)) {
			alpha = 0.2F;
		} else {
			alpha = 0.5F;
		}
	}

	@Override
	public void tick() {
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
		if (age++ >= maxAge) {
			markDead();
		} else {
			velocityY = velocityY - gravityStrength;
			move(velocityX, velocityY, velocityZ);
			setSpriteForAge(spriteProvider);
		}
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	public static class Factory implements ParticleFactory<HoneyBubbleParticleEffect> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public @Nullable Particle createParticle(HoneyBubbleParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new HoneyBubbleParticle(parameters.ownerId(), world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider);
		}
	}
}
