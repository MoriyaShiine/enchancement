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
import net.minecraft.util.Uuids;

import java.util.UUID;

public record HoneyBubbleParticleEffect(UUID ownerId) implements ParticleEffect {
	public static final MapCodec<HoneyBubbleParticleEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Uuids.CODEC.fieldOf("owner_id").forGetter(HoneyBubbleParticleEffect::ownerId)
	).apply(instance, HoneyBubbleParticleEffect::new));
	public static final PacketCodec<RegistryByteBuf, HoneyBubbleParticleEffect> PACKET_CODEC = PacketCodec.tuple(Uuids.PACKET_CODEC, HoneyBubbleParticleEffect::ownerId, HoneyBubbleParticleEffect::new);

	@Override
	public ParticleType<?> getType() {
		return ModParticleTypes.HONEY_BUBBLE;
	}
}
