/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.util.Identifier.of;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
	public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		getOrCreateTagBuilder(ModItemTags.CANNOT_AUTOMATICALLY_CONSUME)
				.addOptionalTag(ConventionalItemTags.RAW_FISH_FOODS)
				.addOptionalTag(ConventionalItemTags.RAW_MEAT_FOODS)
				.addOptionalTag(ConventionalItemTags.FOOD_POISONING_FOODS)
				.add(Items.CHORUS_FRUIT)
				.add(Items.ENCHANTED_GOLDEN_APPLE)
				.add(Items.GOLDEN_APPLE)
				.add(Items.OMINOUS_BOTTLE)
				.addOptionalTag(of("c", "foods/doughs"))
				.addOptionalTag(of("c", "foods/pastas"))
				.addOptional(of("farmersdelight", "dog_food"))
				.addOptional(of("farmersdelight", "pie_crust"))
				.addOptional(of("spelunkery", "portal_fluid_bottle"));
		getOrCreateTagBuilder(ModItemTags.DEFAULT_ENCHANTING_MATERIAL)
				.add(Items.AMETHYST_SHARD);
		getOrCreateTagBuilder(ModItemTags.NO_LOYALTY)
				.addOptional(of("impaled", "pitchfork"));
		getOrCreateTagBuilder(ModItemTags.RETAINS_DURABILITY)
				.add(Items.WOLF_ARMOR)
				.addOptionalTag(of("create", "sandpaper"))
				.addOptional(of("create", "super_glue"));
		getOrCreateTagBuilder(ModItemTags.WEAKLY_ENCHANTED)
				.add(Items.LEATHER_HORSE_ARMOR)
				.add(Items.IRON_HORSE_ARMOR)
				.addOptional(of("impaled", "pitchfork"));
	}
}
