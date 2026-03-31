/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.particle.HoneyBubbleParticleOption;
import moriyashiine.enchancement.common.particle.SparkParticleOption;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerParticleType;

public class ModParticleTypes {
	public static final SimpleParticleType BRIMSTONE_BUBBLE = registerParticleType("brimstone_bubble", FabricParticleTypes.simple());
	public static final SimpleParticleType CHISELED_ENCHANT = registerParticleType("chiseled_enchant", FabricParticleTypes.simple());
	public static final SimpleParticleType CRITICAL_TIPPER = registerParticleType("critical_tipper", FabricParticleTypes.simple());
	public static final ParticleType<HoneyBubbleParticleOption> HONEY_BUBBLE = registerParticleType("honey_bubble", FabricParticleTypes.complex(HoneyBubbleParticleOption.CODEC, HoneyBubbleParticleOption.PACKET_CODEC));
	public static final SimpleParticleType SHORT_SMALL_FLAME = registerParticleType("short_small_flame", FabricParticleTypes.simple());
	public static final ParticleType<SparkParticleOption> SPARK = registerParticleType("spark", FabricParticleTypes.complex(SparkParticleOption.CODEC, SparkParticleOption.PACKET_CODEC));
	public static final SimpleParticleType VELOCITY_LINE = registerParticleType("velocity_line", FabricParticleTypes.simple());

	public static void init() {
	}
}
