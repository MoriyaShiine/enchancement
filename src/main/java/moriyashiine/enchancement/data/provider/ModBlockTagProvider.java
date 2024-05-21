/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.init.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.Blocks;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public ModBlockTagProvider(FabricDataOutput output) {
		super(output, CompletableFuture.supplyAsync(BuiltinRegistries::createWrapperLookup));
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		getOrCreateTagBuilder(ModTags.Blocks.BURIABLE)
				.addOptionalTag(BlockTags.SHOVEL_MINEABLE)
				.addOptionalTag(BlockTags.NYLIUM)
				.add(Blocks.COBWEB)
				.add(Blocks.NETHERRACK)
				.add(Blocks.POWDER_SNOW);
		getOrCreateTagBuilder(ModTags.Blocks.NETHER_ORES)
				.addOptionalTag(ConventionalBlockTags.NETHERITE_SCRAP_ORES)
				.addOptionalTag(ConventionalBlockTags.QUARTZ_ORES)
				.add(Blocks.NETHER_GOLD_ORE)
				.addOptional(Identifier.tryParse("cinderscapes:sulfur_ore"));
		getOrCreateTagBuilder(ModTags.Blocks.SMELTS_SELF)
				.add(Blocks.NETHER_GOLD_ORE);
	}
}
