/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModParticleTypes {
	public static final SimpleParticleType BRIMSTONE_BUBBLE = FabricParticleTypes.simple();

	public static void init() {
		Registry.register(Registries.PARTICLE_TYPE, Enchancement.id("brimstone_bubble"), BRIMSTONE_BUBBLE);
	}
}
