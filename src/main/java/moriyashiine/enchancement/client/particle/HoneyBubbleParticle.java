/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.particle;

import moriyashiine.enchancement.common.particle.HoneyBubbleParticleOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class HoneyBubbleParticle extends SingleQuadParticle {
	private final SpriteSet sprites;

	public HoneyBubbleParticle(UUID ownerId, ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
		super(level, x, y, z, sprites.first());
		this.sprites = sprites;
		this.xd = xa;
		this.yd = ya;
		this.zd = za;
		lifetime = 3;
		gravity = 0.008F;
		Player player = Minecraft.getInstance().player;
		if (player != null && player.getUUID().equals(ownerId)) {
			alpha = 0.2F;
		} else {
			alpha = 0.5F;
		}
	}

	@Override
	public void tick() {
		xo = x;
		yo = y;
		zo = z;
		if (age++ >= lifetime) {
			remove();
		} else {
			yd = yd - gravity;
			move(xd, yd, zd);
			setSpriteFromAge(sprites);
		}
	}

	@Override
	protected Layer getLayer() {
		return Layer.TRANSLUCENT;
	}

	public record Provider(SpriteSet sprites) implements ParticleProvider<HoneyBubbleParticleOption> {
		@Override
		public Particle createParticle(HoneyBubbleParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
			return new HoneyBubbleParticle(options.ownerId(), level, x, y, z, xAux, yAux, zAux, sprites());
		}
	}
}
