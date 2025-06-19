/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.tag.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.util.Identifier.of;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		valueLookupBuilder(ModBlockTags.BURIABLE)
				.addOptionalTag(BlockTags.HOE_MINEABLE)
				.addOptionalTag(BlockTags.SHOVEL_MINEABLE)
				.addOptionalTag(BlockTags.NYLIUM)
				.add(Blocks.COBWEB)
				.add(Blocks.NETHERRACK)
				.add(Blocks.POWDER_SNOW);
		valueLookupBuilder(ModBlockTags.NETHER_ORES)
				.addOptionalTag(ConventionalBlockTags.NETHERITE_SCRAP_ORES)
				.addOptionalTag(ConventionalBlockTags.QUARTZ_ORES)
				.add(Blocks.NETHER_GOLD_ORE);
		builder(ModBlockTags.NETHER_ORES)
				.addOptional(key(of("cinderscapes", "sulfur_ore")));
		valueLookupBuilder(ModBlockTags.SMELTS_SELF)
				.addOptionalTag(BlockTags.LEAVES)
				.add(Blocks.NETHER_GOLD_ORE);
		valueLookupBuilder(ModBlockTags.UNSTICKABLE)
				.addOptionalTag(BlockTags.ICE);
	}

	private static RegistryKey<Block> key(Identifier id) {
		return RegistryKey.of(RegistryKeys.BLOCK, id);
	}
}
