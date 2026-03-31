/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class ShortFlameParticle extends FlameParticle {
	public ShortFlameParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
		super(level, x, y, z, xa, ya, za, sprites.first());
		lifetime /= 4;
	}

	public record SmallProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
			ShortFlameParticle shortFlameParticle = new ShortFlameParticle(level, x, y, z, xAux, yAux, zAux, sprites());
			shortFlameParticle.scale(0.5F);
			return shortFlameParticle;
		}
	}
}
