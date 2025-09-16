/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.particle;

import net.minecraft.client.particle.DamageParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;

public class TintlessDamageParticle extends DamageParticle {
	private final float tint;

	public TintlessDamageParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(world, x, y, z, velocityX, velocityY, velocityZ);
		tint = MathHelper.nextFloat(random, 0.9F, 1);
		resetTint();
	}

	@Override
	public void tick() {
		super.tick();
		resetTint();
	}

	private void resetTint() {
		red = green = blue = tint;
	}

	public record Factory(SpriteProvider spriteProvider) implements ParticleFactory<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			TintlessDamageParticle particle = new TintlessDamageParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setSprite(spriteProvider());
			return particle;
		}
	}
}
