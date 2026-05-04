/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.api.datagen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.reloadlistener.EnchantingMaterialsReloadListener;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class EnchantingMaterialsProvider extends FabricCodecDataProvider<EnchantingMaterialsProvider.DatagenEnchantingMaterial> {
	public EnchantingMaterialsProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(packOutput, registriesFuture, PackOutput.Target.DATA_PACK, EnchantingMaterialsReloadListener.DIRECTORY, DatagenEnchantingMaterial.CODEC);
	}

	@Override
	protected final void configure(BiConsumer<Identifier, DatagenEnchantingMaterial> provider, HolderLookup.Provider registries) {
		configure(((itemId, material) -> provider.accept(itemId, new DatagenEnchantingMaterial(material))));
	}

	protected abstract void configure(Output output);

	protected Identifier id(String id) {
		return Identifier.parse(id);
	}

	protected TagKey<Item> key(String id) {
		return TagKey.create(Registries.ITEM, Identifier.parse(id));
	}

	@FunctionalInterface
	protected interface Output {
		void accept(Identifier itemId, String material);

		default void accept(Identifier itemId, Item material) {
			accept(itemId, BuiltInRegistries.ITEM.getKey(material).toString());
		}

		default void accept(Identifier itemId, TagKey<Item> material) {
			accept(itemId, "#" + material.location());
		}

		default void accept(Item item, Item material) {
			accept(BuiltInRegistries.ITEM.getKey(item), material);
		}

		default void accept(Item item, TagKey<Item> material) {
			accept(BuiltInRegistries.ITEM.getKey(item), material);
		}
	}

	protected record DatagenEnchantingMaterial(String ingredient) {
		public static final Codec<DatagenEnchantingMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.STRING.fieldOf("ingredient").forGetter(DatagenEnchantingMaterial::ingredient)
		).apply(instance, DatagenEnchantingMaterial::new));
	}
}
