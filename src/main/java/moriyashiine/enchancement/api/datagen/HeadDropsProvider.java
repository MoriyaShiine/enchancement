/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.api.datagen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.reloadlistener.HeadDropsReloadListener;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class HeadDropsProvider extends FabricCodecDataProvider<HeadDropsProvider.DatagenHeadDropEntry> {
	public HeadDropsProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(packOutput, registriesFuture, PackOutput.Target.DATA_PACK, HeadDropsReloadListener.DIRECTORY, DatagenHeadDropEntry.CODEC);
	}

	@Override
	protected final void configure(BiConsumer<Identifier, DatagenHeadDropEntry> provider, HolderLookup.Provider registries) {
		configure(((typeId, drop, chance) -> provider.accept(typeId, new DatagenHeadDropEntry(drop, chance))));
	}

	protected abstract void configure(Output output);

	protected Identifier id(String id) {
		return Identifier.parse(id);
	}

	@FunctionalInterface
	protected interface Output {
		void accept(Identifier typeId, Identifier dropId, float chance);

		default void accept(EntityType<?> type, Identifier dropId, float chance) {
			accept(BuiltInRegistries.ENTITY_TYPE.getKey(type), dropId, chance);
		}

		default void accept(Identifier typeId, Item drop, float chance) {
			accept(typeId, BuiltInRegistries.ITEM.getKey(drop), chance);
		}

		default void accept(EntityType<?> type, Item drop, float chance) {
			accept(type, BuiltInRegistries.ITEM.getKey(drop), chance);
		}
	}

	protected record DatagenHeadDropEntry(Identifier drop, float chance) {
		public static final Codec<DatagenHeadDropEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Identifier.CODEC.fieldOf("drop").forGetter(DatagenHeadDropEntry::drop),
				Codec.FLOAT.fieldOf("chance").forGetter(DatagenHeadDropEntry::chance)
		).apply(instance, DatagenHeadDropEntry::new));
	}
}
