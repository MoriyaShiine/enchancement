/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.datagen.provider;

import moriyashiine.enchancement.common.tag.EnchancementItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.ItemIds;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class EnchancementItemTagsProvider extends FabricTagsProvider.ItemTagsProvider {
	public EnchancementItemTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		builder(EnchancementItemTags.BREAKABLE)
				.forceAddTag(ConventionalItemTags.WOLF_ARMORS)
				.addOptionalTag(tagKey("create:sandpaper"))
				.addOptional(key("create:super_glue"));

		builder(EnchancementItemTags.WEAKLY_ENCHANTED)
				.add(ItemIds.LEATHER_HORSE_ARMOR)
				.add(ItemIds.COPPER_HORSE_ARMOR)
				.add(ItemIds.IRON_HORSE_ARMOR);

		builder(EnchancementItemTags.CANNOT_AUTOMATICALLY_CONSUME)
				.forceAddTag(ConventionalItemTags.RAW_FISH_FOODS)
				.forceAddTag(ConventionalItemTags.RAW_MEAT_FOODS)
				.forceAddTag(ConventionalItemTags.FOOD_POISONING_FOODS)
				.addOptionalTag(tagKey("c:foods/doughs"))
				.addOptionalTag(tagKey("c:foods/pastas"))
				.add(ItemIds.CHORUS_FRUIT)
				.add(ItemIds.ENCHANTED_GOLDEN_APPLE)
				.add(ItemIds.GOLDEN_APPLE)
				.add(ItemIds.OMINOUS_BOTTLE)
				.addOptional(key("enderscape:murublight_bracket"))
				.addOptional(key("farmersdelight:dog_food"))
				.addOptional(key("farmersdelight:pie_crust"))
				.addOptional(key("spelunkery:portal_fluid_bottle"));
		builder(EnchancementItemTags.DEFAULT_ENCHANTING_MATERIAL)
				.add(ItemIds.AMETHYST_SHARD);

		builder(EnchancementItemTags.EXCAVATING_ENCHANTABLE)
				.forceAddTag(ItemTags.PICKAXES)
				.forceAddTag(ItemTags.SHOVELS);
	}

	private static TagKey<Item> tagKey(String id) {
		return TagKey.create(Registries.ITEM, Identifier.parse(id));
	}

	private static ResourceKey<Item> key(String id) {
		return ResourceKey.create(Registries.ITEM, Identifier.parse(id));
	}
}
