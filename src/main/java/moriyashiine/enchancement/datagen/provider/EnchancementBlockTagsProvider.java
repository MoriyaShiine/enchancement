/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.datagen.provider;

import moriyashiine.enchancement.common.tag.EnchancementBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemIds;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class EnchancementBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
	public EnchancementBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		builder(EnchancementBlockTags.BURIABLE)
				.forceAddTag(BlockTags.MINEABLE_WITH_HOE)
				.forceAddTag(BlockTags.MINEABLE_WITH_SHOVEL)
				.forceAddTag(BlockTags.NYLIUM)
				.forceAddTag(BlockTags.SNOW)
				.add(BlockItemIds.COBWEB.block())
				.add(BlockItemIds.NETHERRACK.block());
		builder(EnchancementBlockTags.FELLABLE)
				.forceAddTag(BlockTags.LOGS)
				.add(BlockItemIds.MANGROVE_ROOTS.block());
		builder(EnchancementBlockTags.SMELTS_SELF)
				.forceAddTag(BlockTags.LEAVES)
				.add(BlockItemIds.NETHER_GOLD_ORE.block())
				.addOptional(key("enderscape:mirestone_nebulite_ore"))
				.addOptional(key("enderscape:nebulite_ore"))
				.addOptional(key("universal_ores:basalt_gold_ore"))
				.addOptional(key("universal_ores:blackstone_gold_ore"));
		builder(EnchancementBlockTags.UNSTICKABLE)
				.forceAddTag(BlockTags.ICE);

		builder(EnchancementBlockTags.DEEPSLATE_BASE_BLOCKS)
				.forceAddTag(ConventionalBlockTags.ORES_IN_GROUND_DEEPSLATE);
		builder(EnchancementBlockTags.NETHERRACK_BASE_BLOCKS)
				.forceAddTag(ConventionalBlockTags.ORES_IN_GROUND_NETHERRACK)
				.add(BlockItemIds.ANCIENT_DEBRIS.block());
	}

	private static ResourceKey<Block> key(String id) {
		return ResourceKey.create(Registries.BLOCK, Identifier.parse(id));
	}
}
