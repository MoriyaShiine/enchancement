/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.tag.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.util.Identifier.of;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		getOrCreateTagBuilder(ModBlockTags.BURIABLE)
				.addOptionalTag(BlockTags.HOE_MINEABLE)
				.addOptionalTag(BlockTags.SHOVEL_MINEABLE)
				.addOptionalTag(BlockTags.NYLIUM)
				.add(Blocks.COBWEB)
				.add(Blocks.NETHERRACK)
				.add(Blocks.POWDER_SNOW);
		getOrCreateTagBuilder(ModBlockTags.NETHER_ORES)
				.addOptionalTag(ConventionalBlockTags.NETHERITE_SCRAP_ORES)
				.addOptionalTag(ConventionalBlockTags.QUARTZ_ORES)
				.add(Blocks.NETHER_GOLD_ORE)
				.addOptional(of("cinderscapes", "sulfur_ore"));
		getOrCreateTagBuilder(ModBlockTags.SMELTS_SELF)
				.addOptionalTag(BlockTags.LEAVES)
				.add(Blocks.NETHER_GOLD_ORE);
		getOrCreateTagBuilder(ModBlockTags.UNSTICKABLE)
				.addOptionalTag(BlockTags.ICE);
	}
}
