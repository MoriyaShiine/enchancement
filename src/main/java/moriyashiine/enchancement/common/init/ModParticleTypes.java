/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.particle.HoneyBubbleParticleEffect;
import moriyashiine.enchancement.common.particle.SparkParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModParticleTypes {
	public static final SimpleParticleType BRIMSTONE_BUBBLE = FabricParticleTypes.simple();
	public static final ParticleType<HoneyBubbleParticleEffect> HONEY_BUBBLE = FabricParticleTypes.complex(HoneyBubbleParticleEffect.CODEC, HoneyBubbleParticleEffect.PACKET_CODEC);
	public static final ParticleType<SparkParticleEffect> SPARK = FabricParticleTypes.complex(SparkParticleEffect.CODEC, SparkParticleEffect.PACKET_CODEC);
	public static final SimpleParticleType VELOCITY_LINE = FabricParticleTypes.simple();

	public static void init() {
		Registry.register(Registries.PARTICLE_TYPE, Enchancement.id("brimstone_bubble"), BRIMSTONE_BUBBLE);
		Registry.register(Registries.PARTICLE_TYPE, Enchancement.id("honey_bubble"), HONEY_BUBBLE);
		Registry.register(Registries.PARTICLE_TYPE, Enchancement.id("spark"), SPARK);
		Registry.register(Registries.PARTICLE_TYPE, Enchancement.id("velocity_line"), VELOCITY_LINE);
	}
}
