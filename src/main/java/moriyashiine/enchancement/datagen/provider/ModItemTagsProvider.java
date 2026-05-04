/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.datagen.provider;

import moriyashiine.enchancement.common.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends FabricTagsProvider.ItemTagsProvider {
	public ModItemTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
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
				.addOptional(key("enderscape:murublight_bracket"))
				.addOptional(key("farmersdelight:dog_food"))
				.addOptional(key("farmersdelight:pie_crust"))
				.addOptional(key("spelunkery:portal_fluid_bottle"));
		valueLookupBuilder(ModItemTags.DEFAULT_ENCHANTING_MATERIAL)
				.add(Items.AMETHYST_SHARD);
		valueLookupBuilder(ModItemTags.RETAINS_DURABILITY)
				.forceAddTag(ConventionalItemTags.WOLF_ARMORS);
		builder(ModItemTags.RETAINS_DURABILITY)
				.addOptionalTag(tagKey("create:sandpaper"))
				.addOptional(key("create:super_glue"));
		valueLookupBuilder(ModItemTags.WEAKLY_ENCHANTED)
				.add(Items.LEATHER_HORSE_ARMOR)
				.add(Items.COPPER_HORSE_ARMOR)
				.add(Items.IRON_HORSE_ARMOR);
	}

	private static TagKey<Item> tagKey(String id) {
		return TagKey.create(Registries.ITEM, Identifier.parse(id));
	}

	private static ResourceKey<Item> key(String id) {
		return ResourceKey.create(Registries.ITEM, Identifier.parse(id));
	}
}
