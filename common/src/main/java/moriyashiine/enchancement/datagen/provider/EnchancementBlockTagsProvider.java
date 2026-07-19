package moriyashiine.enchancement.datagen.provider;

import moriyashiine.enchancement.common.tag.EnchancementBlockTags;
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

public class EnchancementBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {
	public EnchancementBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		valueLookupBuilder(EnchancementBlockTags.BURIABLE)
				.forceAddTag(BlockTags.MINEABLE_WITH_HOE)
				.forceAddTag(BlockTags.MINEABLE_WITH_SHOVEL)
				.forceAddTag(BlockTags.NYLIUM)
				.forceAddTag(BlockTags.SNOW)
				.add(Blocks.COBWEB)
				.add(Blocks.NETHERRACK);
		valueLookupBuilder(EnchancementBlockTags.FELLABLE)
				.forceAddTag(BlockTags.LOGS)
				.add(Blocks.MANGROVE_ROOTS);
		valueLookupBuilder(EnchancementBlockTags.SMELTS_SELF)
				.forceAddTag(BlockTags.LEAVES)
				.add(Blocks.NETHER_GOLD_ORE);
		builder(EnchancementBlockTags.SMELTS_SELF)
				.addOptional(key("enderscape:mirestone_nebulite_ore"))
				.addOptional(key("enderscape:nebulite_ore"))
				.addOptional(key("universal_ores:basalt_gold_ore"))
				.addOptional(key("universal_ores:blackstone_gold_ore"));
		valueLookupBuilder(EnchancementBlockTags.UNSTICKABLE)
				.forceAddTag(BlockTags.ICE);

		valueLookupBuilder(EnchancementBlockTags.DEEPSLATE_BASE_BLOCKS)
				.forceAddTag(ConventionalBlockTags.ORES_IN_GROUND_DEEPSLATE);
		valueLookupBuilder(EnchancementBlockTags.NETHERRACK_BASE_BLOCKS)
				.forceAddTag(ConventionalBlockTags.ORES_IN_GROUND_NETHERRACK)
				.add(Blocks.ANCIENT_DEBRIS);
	}

	private static ResourceKey<Block> key(String id) {
		return ResourceKey.create(Registries.BLOCK, Identifier.parse(id));
	}
}
