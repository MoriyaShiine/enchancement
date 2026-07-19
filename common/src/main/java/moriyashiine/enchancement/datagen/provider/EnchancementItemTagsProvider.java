package moriyashiine.enchancement.datagen.provider;

import moriyashiine.enchancement.common.tag.EnchancementItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

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

		valueLookupBuilder(EnchancementItemTags.WEAKLY_ENCHANTED)
				.add(Items.LEATHER_HORSE_ARMOR)
				.add(Items.COPPER_HORSE_ARMOR)
				.add(Items.IRON_HORSE_ARMOR);

		valueLookupBuilder(EnchancementItemTags.CANNOT_AUTOMATICALLY_CONSUME)
				.forceAddTag(ConventionalItemTags.RAW_FISH_FOODS)
				.forceAddTag(ConventionalItemTags.RAW_MEAT_FOODS)
				.forceAddTag(ConventionalItemTags.FOOD_POISONING_FOODS)
				.addOptionalTag(tagKey("c:foods/doughs"))
				.addOptionalTag(tagKey("c:foods/pastas"))
				.add(Items.CHORUS_FRUIT)
				.add(Items.ENCHANTED_GOLDEN_APPLE)
				.add(Items.GOLDEN_APPLE)
				.add(Items.OMINOUS_BOTTLE);
		builder(EnchancementItemTags.CANNOT_AUTOMATICALLY_CONSUME)
				.addOptional(key("enderscape:murublight_bracket"))
				.addOptional(key("farmersdelight:dog_food"))
				.addOptional(key("farmersdelight:pie_crust"))
				.addOptional(key("spelunkery:portal_fluid_bottle"));
		valueLookupBuilder(EnchancementItemTags.DEFAULT_ENCHANTING_MATERIAL)
				.add(Items.AMETHYST_SHARD);

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
