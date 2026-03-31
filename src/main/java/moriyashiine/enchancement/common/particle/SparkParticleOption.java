/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.particle;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public record SparkParticleOption(Vec3 destination) implements ParticleOptions {
	public static final MapCodec<SparkParticleOption> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Vec3.CODEC.fieldOf("destination").forGetter(SparkParticleOption::destination)
	).apply(instance, SparkParticleOption::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, SparkParticleOption> PACKET_CODEC = StreamCodec.composite(Vec3.STREAM_CODEC, SparkParticleOption::destination, SparkParticleOption::new);

	@Override
	public ParticleType<?> getType() {
		return ModParticleTypes.SPARK;
	}
}
