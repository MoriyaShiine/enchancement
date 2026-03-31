/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.data.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.reloadlistener.EnchantingMaterialsReloadListener;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModEnchantingMaterialsProvider extends FabricCodecDataProvider<ModEnchantingMaterialsProvider.DatagenEnchantingMaterial> {
	public ModEnchantingMaterialsProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(packOutput, registriesFuture, PackOutput.Target.DATA_PACK, EnchantingMaterialsReloadListener.DIRECTORY, DatagenEnchantingMaterial.CODEC);
	}

	@Override
	protected final void configure(BiConsumer<Identifier, DatagenEnchantingMaterial> provider, HolderLookup.Provider registries) {
		addEnchantingMaterials(((itemId, material) -> provider.accept(itemId, new DatagenEnchantingMaterial(material))));
	}

	protected void addEnchantingMaterials(Output output) {
		output.accept(Items.BOW, Items.STRING);
		output.accept(Items.BRUSH, ItemTags.COPPER_TOOL_MATERIALS);
		output.accept(Items.CARROT_ON_A_STICK, Items.STRING);
		output.accept(Items.COPPER_HORSE_ARMOR, ItemTags.REPAIRS_COPPER_ARMOR);
		output.accept(Items.CROSSBOW, Items.STRING);
		output.accept(Items.DIAMOND_HORSE_ARMOR, ItemTags.REPAIRS_DIAMOND_ARMOR);
		output.accept(Items.FISHING_ROD, Items.STRING);
		output.accept(Items.FLINT_AND_STEEL, ItemTags.IRON_TOOL_MATERIALS);
		output.accept(Items.GOLDEN_HORSE_ARMOR, ItemTags.REPAIRS_GOLD_ARMOR);
		output.accept(Items.IRON_HORSE_ARMOR, ItemTags.REPAIRS_IRON_ARMOR);
		output.accept(Items.LEATHER_HORSE_ARMOR, ItemTags.REPAIRS_LEATHER_ARMOR);
		output.accept(Items.NETHERITE_HORSE_ARMOR, ItemTags.REPAIRS_DIAMOND_ARMOR);
		output.accept(Items.SHEARS, ItemTags.IRON_TOOL_MATERIALS);
		output.accept(Items.SHIELD, ItemTags.IRON_TOOL_MATERIALS);
		output.accept(Items.TRIDENT, Items.PRISMARINE_SHARD);
		output.accept(Items.WARPED_FUNGUS_ON_A_STICK, Items.STRING);

		output.accept(id("enderscape:dagger"), key("c:ingots/shadoline"));
		output.accept(id("enderscape:magnia_attractor"), key("c:ingots/shadoline"));
		output.accept(id("enderscape:mirror"), key("c:gems/nebulite"));
	}

	protected Identifier id(String id) {
		return Identifier.parse(id);
	}

	protected TagKey<Item> key(String id) {
		return TagKey.create(Registries.ITEM, Identifier.parse(id));
	}

	@Override
	public String getName() {
		return Enchancement.MOD_ID + "_enchanting_materials";
	}

	@FunctionalInterface
	public interface Output {
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
