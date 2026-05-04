/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.api.datagen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.reloadlistener.BaseBlocksReloadListener;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class BaseBlocksProvider extends FabricCodecDataProvider<BaseBlocksProvider.DatagenBaseBlock> {
	public BaseBlocksProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(packOutput, registriesFuture, PackOutput.Target.DATA_PACK, BaseBlocksReloadListener.DIRECTORY, DatagenBaseBlock.CODEC);
	}

	@Override
	protected final void configure(BiConsumer<Identifier, DatagenBaseBlock> provider, HolderLookup.Provider registries) {
		configure(((blockId, baseId) -> provider.accept(blockId, new DatagenBaseBlock(baseId))));
	}

	protected abstract void configure(Output output);

	protected Identifier id(String id) {
		return Identifier.parse(id);
	}

	@FunctionalInterface
	protected interface Output {
		void accept(Identifier blockId, Identifier baseId);

		default void accept(Block block, Identifier baseId) {
			accept(BuiltInRegistries.BLOCK.getKey(block), baseId);
		}

		default void accept(Identifier blockId, Block base) {
			accept(blockId, BuiltInRegistries.BLOCK.getKey(base));
		}

		default void accept(Block block, Block base) {
			accept(block, BuiltInRegistries.BLOCK.getKey(base));
		}
	}

	protected record DatagenBaseBlock(Identifier base) {
		public static final Codec<DatagenBaseBlock> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Identifier.CODEC.fieldOf("base").forGetter(DatagenBaseBlock::base)
		).apply(instance, DatagenBaseBlock::new));
	}
}
