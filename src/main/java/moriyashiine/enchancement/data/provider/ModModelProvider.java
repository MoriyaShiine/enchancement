/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.client.data.TextureMap;

public class ModModelProvider extends FabricModelProvider {
	public ModModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator generator) {
	}

	@Override
	public void generateItemModels(ItemModelGenerator generator) {
		Models.CROSSBOW.upload(Enchancement.id("item/crossbow_amethyst"), TextureMap.layer0(Enchancement.id("item/crossbow_amethyst")), generator.modelCollector);
		Models.CROSSBOW.upload(Enchancement.id("item/crossbow_torch"), TextureMap.layer0(Enchancement.id("item/crossbow_torch")), generator.modelCollector);
		for (int i = 0; i < 6; i++) {
			Models.CROSSBOW.upload(Enchancement.id("item/crossbow_brimstone_" + i), TextureMap.layer0(Enchancement.id("item/crossbow_brimstone_" + i)), generator.modelCollector);
		}
	}
}
