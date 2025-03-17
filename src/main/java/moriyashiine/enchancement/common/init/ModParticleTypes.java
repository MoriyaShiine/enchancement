/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.particle.HoneyBubbleParticleEffect;
import moriyashiine.enchancement.common.particle.SparkParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerParticleType;

public class ModParticleTypes {
	public static final SimpleParticleType BRIMSTONE_BUBBLE = registerParticleType("brimstone_bubble", FabricParticleTypes.simple());
	public static final SimpleParticleType CRITICAL_TIPPER = registerParticleType("critical_tipper", FabricParticleTypes.simple());
	public static final ParticleType<HoneyBubbleParticleEffect> HONEY_BUBBLE = registerParticleType("honey_bubble", FabricParticleTypes.complex(HoneyBubbleParticleEffect.CODEC, HoneyBubbleParticleEffect.PACKET_CODEC));
	public static final ParticleType<SparkParticleEffect> SPARK = registerParticleType("spark", FabricParticleTypes.complex(SparkParticleEffect.CODEC, SparkParticleEffect.PACKET_CODEC));
	public static final SimpleParticleType VELOCITY_LINE = registerParticleType("velocity_line", FabricParticleTypes.simple());

	public static void init() {
	}
}
