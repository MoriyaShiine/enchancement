package moriyashiine.enchancement.api.datagen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.reloadlistener.EnchantingMaterialsReloadListener;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
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
		configure(((item, material) -> provider.accept(item.identifier(), new DatagenEnchantingMaterial(material))));
	}

	protected ResourceKey<Item> key(String id) {
		return ResourceKey.create(Registries.ITEM, Identifier.parse(id));
	}

	protected TagKey<Item> tagKey(String id) {
		return TagKey.create(Registries.ITEM, Identifier.parse(id));
	}

	protected abstract void configure(Output output);

	@FunctionalInterface
	protected interface Output {
		void accept(ResourceKey<Item> item, String material);

		default void accept(ResourceKey<Item> item, ResourceKey<Item> material) {
			accept(item, material.identifier().toString());
		}

		default void accept(ResourceKey<Item> item, TagKey<Item> material) {
			accept(item, "#" + material.location());
		}

		default void accept(Item item, Item material) {
			accept(item.builtInRegistryHolder().key(), material.builtInRegistryHolder().key());
		}

		default void accept(Item item, TagKey<Item> material) {
			accept(item.builtInRegistryHolder().key(), material);
		}
	}

	protected record DatagenEnchantingMaterial(String ingredient) {
		private static final Codec<DatagenEnchantingMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.STRING.fieldOf("ingredient").forGetter(DatagenEnchantingMaterial::ingredient)
		).apply(instance, DatagenEnchantingMaterial::new));
	}
}
