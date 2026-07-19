/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.api.datagen;

import moriyashiine.enchancement.common.reloadlistener.HeadDropsReloadListener;
import moriyashiine.enchancement.common.util.enchantment.HeadDrop;
import moriyashiine.strawberrylib.api.objects.records.BlockItemId;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class HeadDropsProvider extends FabricCodecDataProvider<HeadDrop> {
	public HeadDropsProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(packOutput, registriesFuture, PackOutput.Target.DATA_PACK, HeadDropsReloadListener.DIRECTORY, HeadDrop.CODEC);
	}

	@Override
	protected final void configure(BiConsumer<Identifier, HeadDrop> provider, HolderLookup.Provider registries) {
		configure(((type, drop, chance) -> provider.accept(type.identifier(), new HeadDrop(drop, chance))));
	}

	protected ResourceKey<EntityType<?>> entityTypeKey(String id) {
		return ResourceKey.create(Registries.ENTITY_TYPE, Identifier.parse(id));
	}

	protected ResourceKey<Item> itemKey(String id) {
		return ResourceKey.create(Registries.ITEM, Identifier.parse(id));
	}

	protected abstract void configure(Output output);

	@FunctionalInterface
	protected interface Output {
		void accept(ResourceKey<EntityType<?>> type, ResourceKey<Item> drop, float chance);

		default void accept(ResourceKey<EntityType<?>> type, BlockItemId drop, float chance) {
			accept(type, drop.item(), chance);
		}

		default void accept(EntityType<?> type, Item drop, float chance) {
			accept(type.builtInRegistryHolder().key(), drop.builtInRegistryHolder().key(), chance);
		}
	}
}
