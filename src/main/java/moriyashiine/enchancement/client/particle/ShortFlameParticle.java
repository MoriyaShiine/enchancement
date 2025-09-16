/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.particle;

import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

public class ShortFlameParticle extends FlameParticle {
	public ShortFlameParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(world, x, y, z, velocityX, velocityY, velocityZ);
		maxAge /= 4;
	}

	public record SmallFactory(SpriteProvider spriteProvider) implements ParticleFactory<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			ShortFlameParticle shortFlameParticle = new ShortFlameParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			shortFlameParticle.setSprite(spriteProvider());
			shortFlameParticle.scale(0.5F);
			return shortFlameParticle;
		}
	}
}
