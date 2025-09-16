/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.particle;

import net.minecraft.client.particle.ConnectionParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;

public class PurpleConnectionParticle extends ConnectionParticle {
	public PurpleConnectionParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(world, x, y, z, velocityX, velocityY, velocityZ);
		setColor(MathHelper.nextFloat(random, 0.7F, 0.8F), 0, MathHelper.nextFloat(random, 0.8F, 0.9F));
	}

	public record Factory(SpriteProvider spriteProvider) implements ParticleFactory<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			PurpleConnectionParticle purpleConnectionParticle = new PurpleConnectionParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			purpleConnectionParticle.setSprite(spriteProvider());
			return purpleConnectionParticle;
		}
	}
}
