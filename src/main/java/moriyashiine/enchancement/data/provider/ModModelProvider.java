/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.sprite.Material;

public class ModModelProvider extends FabricModelProvider {
	public ModModelProvider(FabricPackOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators generators) {
	}

	@Override
	public void generateItemModels(ItemModelGenerators generators) {
		ModelTemplates.CROSSBOW.create(Enchancement.id("item/crossbow_amethyst"), TextureMapping.layer0(new Material(Enchancement.id("item/crossbow_amethyst"))), generators.modelOutput);
		ModelTemplates.CROSSBOW.create(Enchancement.id("item/crossbow_torch"), TextureMapping.layer0(new Material(Enchancement.id("item/crossbow_torch"))), generators.modelOutput);
		for (int i = 0; i < 6; i++) {
			ModelTemplates.CROSSBOW.create(Enchancement.id("item/crossbow_brimstone_" + i), TextureMapping.layer0(new Material(Enchancement.id("item/crossbow_brimstone_" + i))), generators.modelOutput);
		}
	}
}
