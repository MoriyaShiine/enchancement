/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.datagen;

import moriyashiine.enchancement.common.init.EnchancementDamageTypes;
import moriyashiine.enchancement.common.init.EnchancementEnchantments;
import moriyashiine.enchancement.datagen.provider.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class EnchancementDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(EnchancementBaseBlocksProvider::new);
		pack.addProvider(EnchancementBlockTagsProvider::new);
		pack.addProvider(EnchancementDamageTypeTagsProvider::new);
		pack.addProvider(EnchancementDynamicRegistryProvider::new);
		pack.addProvider(EnchancementEnchantingMaterialsProvider::new);
		pack.addProvider(EnchancementEnchantmentTagsProvider::new);
		pack.addProvider(EnchancementEntityTypeTagsProvider::new);
		pack.addProvider(EnchancementHeadDropsProvider::new);
		pack.addProvider(EnchancementItemTagsProvider::new);
		pack.addProvider(EnchancementMobEffectTagsProvider::new);
		pack.addProvider(EnchancementModelProvider::new);
		pack.addProvider(EnchancementSoundsProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.DAMAGE_TYPE, EnchancementDamageTypes::bootstrap);
		registryBuilder.add(Registries.ENCHANTMENT, EnchancementEnchantments::bootstrap);
	}
}
