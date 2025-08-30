/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.util.Identifier.of;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
	public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		valueLookupBuilder(ModItemTags.CANNOT_AUTOMATICALLY_CONSUME)
				.forceAddTag(ConventionalItemTags.RAW_FISH_FOODS)
				.forceAddTag(ConventionalItemTags.RAW_MEAT_FOODS)
				.forceAddTag(ConventionalItemTags.FOOD_POISONING_FOODS)
				.add(Items.CHORUS_FRUIT)
				.add(Items.ENCHANTED_GOLDEN_APPLE)
				.add(Items.GOLDEN_APPLE)
				.add(Items.OMINOUS_BOTTLE);
		builder(ModItemTags.CANNOT_AUTOMATICALLY_CONSUME)
				.addOptionalTag(tagKey("c:foods/doughs"))
				.addOptionalTag(tagKey("c:foods/pastas"))
				.addOptional(key("farmersdelight:dog_food"))
				.addOptional(key("farmersdelight:pie_crust"))
				.addOptional(key("spelunkery:portal_fluid_bottle"));
		valueLookupBuilder(ModItemTags.DEFAULT_ENCHANTING_MATERIAL)
				.add(Items.AMETHYST_SHARD);
		valueLookupBuilder(ModItemTags.RETAINS_DURABILITY)
				.add(Items.WOLF_ARMOR);
		builder(ModItemTags.RETAINS_DURABILITY)
				.addOptionalTag(tagKey("create:sandpaper"))
				.addOptional(key("create:super_glue"));
		valueLookupBuilder(ModItemTags.WEAKLY_ENCHANTED)
				.add(Items.LEATHER_HORSE_ARMOR)
				.add(Items.IRON_HORSE_ARMOR);
	}

	private static TagKey<Item> tagKey(String id) {
		return TagKey.of(RegistryKeys.ITEM, of(id));
	}

	private static RegistryKey<Item> key(String id) {
		return RegistryKey.of(RegistryKeys.ITEM, of(id));
	}
}
