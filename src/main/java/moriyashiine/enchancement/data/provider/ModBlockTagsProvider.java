/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.tag.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
	public ModBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		valueLookupBuilder(ModBlockTags.BURIABLE)
				.forceAddTag(BlockTags.MINEABLE_WITH_HOE)
				.forceAddTag(BlockTags.MINEABLE_WITH_SHOVEL)
				.forceAddTag(BlockTags.NYLIUM)
				.forceAddTag(BlockTags.SNOW)
				.add(Blocks.COBWEB)
				.add(Blocks.NETHERRACK);
		valueLookupBuilder(ModBlockTags.NETHER_ORES)
				.forceAddTag(ConventionalBlockTags.ORES_IN_GROUND_NETHERRACK)
				.forceAddTag(ConventionalBlockTags.NETHERITE_SCRAP_ORES);
		valueLookupBuilder(ModBlockTags.SMELTS_SELF)
				.forceAddTag(BlockTags.LEAVES)
				.add(Blocks.NETHER_GOLD_ORE);
		builder(ModBlockTags.SMELTS_SELF)
				.addOptional(key("enderscape:mirestone_nebulite_ore"))
				.addOptional(key("enderscape:nebulite_ore"))
				.addOptional(key("universal_ores:basalt_gold_ore"))
				.addOptional(key("universal_ores:blackstone_gold_ore"));
		valueLookupBuilder(ModBlockTags.UNSTICKABLE)
				.forceAddTag(BlockTags.ICE);
	}

	private static ResourceKey<Block> key(String id) {
		return ResourceKey.create(Registries.BLOCK, Identifier.parse(id));
	}
}
