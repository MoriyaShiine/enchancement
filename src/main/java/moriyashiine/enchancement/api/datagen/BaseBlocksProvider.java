/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.api.datagen;

import moriyashiine.enchancement.common.reloadlistener.BaseBlocksReloadListener;
import moriyashiine.enchancement.common.util.enchantment.BaseBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class BaseBlocksProvider extends FabricCodecDataProvider<BaseBlock> {
	public BaseBlocksProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(packOutput, registriesFuture, PackOutput.Target.DATA_PACK, BaseBlocksReloadListener.DIRECTORY, BaseBlock.CODEC);
	}

	@Override
	protected final void configure(BiConsumer<Identifier, BaseBlock> provider, HolderLookup.Provider registries) {
		configure(((block, base) -> provider.accept(block.identifier(), new BaseBlock(base))));
	}

	protected ResourceKey<Block> key(String id) {
		return ResourceKey.create(Registries.BLOCK, Identifier.parse(id));
	}

	protected abstract void configure(Output output);

	@FunctionalInterface
	protected interface Output {
		void accept(ResourceKey<Block> block, ResourceKey<Block> base);

		default void accept(BlockItemId block, ResourceKey<Block> base) {
			accept(block.block(), base);
		}

		default void accept(ResourceKey<Block> block, BlockItemId base) {
			accept(block, base.block());
		}

		default void accept(BlockItemId block, BlockItemId base) {
			accept(block.block(), base.block());
		}
	}
}
