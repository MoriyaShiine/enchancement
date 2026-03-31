/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FlyTowardsPositionParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class PurpleFlyTowardsPositionParticle extends FlyTowardsPositionParticle {
	public PurpleFlyTowardsPositionParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
		super(level, x, y, z, xa, ya, za, sprites.first());
		setColor(Mth.nextFloat(random, 0.7F, 0.8F), 0, Mth.nextFloat(random, 0.8F, 0.9F));
	}

	public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
			return new PurpleFlyTowardsPositionParticle(level, x, y, z, xAux, yAux, zAux, sprites());
		}
	}
}
