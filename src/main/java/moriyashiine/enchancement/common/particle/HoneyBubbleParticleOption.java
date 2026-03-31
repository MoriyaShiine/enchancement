/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.particle;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModParticleTypes;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.UUID;

public record HoneyBubbleParticleOption(UUID ownerId) implements ParticleOptions {
	public static final MapCodec<HoneyBubbleParticleOption> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			UUIDUtil.AUTHLIB_CODEC.fieldOf("owner_id").forGetter(HoneyBubbleParticleOption::ownerId)
	).apply(instance, HoneyBubbleParticleOption::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, HoneyBubbleParticleOption> PACKET_CODEC = StreamCodec.composite(UUIDUtil.STREAM_CODEC, HoneyBubbleParticleOption::ownerId, HoneyBubbleParticleOption::new);

	@Override
	public ParticleType<?> getType() {
		return ModParticleTypes.HONEY_BUBBLE;
	}
}
