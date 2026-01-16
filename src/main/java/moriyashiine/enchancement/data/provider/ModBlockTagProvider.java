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

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		valueLookupBuilder(ModBlockTags.BURIABLE)
				.forceAddTag(BlockTags.HOE_MINEABLE)
				.forceAddTag(BlockTags.SHOVEL_MINEABLE)
				.forceAddTag(BlockTags.NYLIUM)
				.add(Blocks.COBWEB)
				.add(Blocks.NETHERRACK)
				.add(Blocks.POWDER_SNOW);
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

	private static RegistryKey<Block> key(String id) {
		return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(id));
	}
}
