/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.particle;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Quaternionf;

public class VelocityLineParticle extends SingleQuadParticle {
	private final boolean vertical;
	private final float yaw;

	public VelocityLineParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet spriteProvider) {
		super(level, x, y, z, spriteProvider.first());
		vertical = ya != 0;
		if (vertical) {
			roll = oRoll = Mth.HALF_PI;
		}
		yaw = (float) Mth.atan2(xa, za) + Mth.HALF_PI;
		lifetime = 3;
		setColor(Mth.nextFloat(random, 0.8F, 1), Mth.nextFloat(random, 0.8F, 1), 0.5F);
		alpha = Mth.nextFloat(random, 0.6F, 0.8F);
	}

	@Override
	public void tick() {
		alpha -= 0.2F;
		if (age++ >= lifetime) {
			remove();
		}
	}

	@Override
	public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
		if (vertical) {
			super.extract(particleTypeRenderState, camera, partialTickTime);
		} else {
			Quaternionf quaternionf = new Quaternionf();
			quaternionf.rotationYXZ(yaw, 0, 0);
			extractRotatedQuad(particleTypeRenderState, camera, quaternionf, partialTickTime);
			quaternionf.rotationYXZ(yaw, Mth.PI, 0);
			extractRotatedQuad(particleTypeRenderState, camera, quaternionf, partialTickTime);
		}
	}

	@Override
	protected Layer getLayer() {
		return Layer.TRANSLUCENT;
	}

	@Override
	public FacingCameraMode getFacingCameraMode() {
		return FacingCameraMode.LOOKAT_Y;
	}

	public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
			return new VelocityLineParticle(level, x, y, z, xAux, yAux, zAux, sprites());
		}
	}
}
