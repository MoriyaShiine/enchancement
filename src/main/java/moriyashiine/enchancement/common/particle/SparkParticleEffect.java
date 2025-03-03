/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.particle;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModParticleTypes;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.Vec3d;

public record SparkParticleEffect(Vec3d destination) implements ParticleEffect {
	public static final MapCodec<SparkParticleEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Vec3d.CODEC.fieldOf("destination").forGetter(SparkParticleEffect::destination)
	).apply(instance, SparkParticleEffect::new));
	public static final PacketCodec<RegistryByteBuf, SparkParticleEffect> PACKET_CODEC = PacketCodec.tuple(Vec3d.PACKET_CODEC, SparkParticleEffect::destination, SparkParticleEffect::new);

	@Override
	public ParticleType<?> getType() {
		return ModParticleTypes.SPARK;
	}
}
