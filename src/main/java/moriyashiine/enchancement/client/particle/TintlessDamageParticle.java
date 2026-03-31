/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CritParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class TintlessDamageParticle extends CritParticle {
	private final float tint;

	public TintlessDamageParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
		super(level, x, y, z, xa, ya, za, sprites.first());
		tint = Mth.nextFloat(random, 0.9F, 1);
		resetTint();
	}

	@Override
	public void tick() {
		super.tick();
		resetTint();
	}

	private void resetTint() {
		rCol = gCol = bCol = tint;
	}

	public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
			return new TintlessDamageParticle(level, x, y, z, xAux, yAux, zAux, sprites());
		}
	}
}
