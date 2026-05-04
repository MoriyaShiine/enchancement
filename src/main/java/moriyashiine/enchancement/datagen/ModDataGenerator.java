/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.datagen;

import moriyashiine.enchancement.common.init.ModDamageTypes;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.datagen.provider.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class ModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModBaseBlocksProvider::new);
		pack.addProvider(ModBlockTagsProvider::new);
		pack.addProvider(ModDamageTypeTagsProvider::new);
		pack.addProvider(ModDynamicRegistryProvider::new);
		pack.addProvider(ModEnchantingMaterialsProvider::new);
		pack.addProvider(ModEnchantmentTagsProvider::new);
		pack.addProvider(ModEntityTypeTagsProvider::new);
		pack.addProvider(ModHeadDropsProvider::new);
		pack.addProvider(ModItemTagsProvider::new);
		pack.addProvider(ModMobEffectTagsProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModSoundsProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap);
		registryBuilder.add(Registries.ENCHANTMENT, ModEnchantments::bootstrap);
	}
}
