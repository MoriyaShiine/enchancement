/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.init.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.item.Items;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.util.Identifier.tryParse;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
	public ModItemTagProvider(FabricDataOutput output) {
		super(output, CompletableFuture.supplyAsync(BuiltinRegistries::createWrapperLookup));
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		getOrCreateTagBuilder(ModTags.Items.CANNOT_ASSIMILATE)
				.addOptionalTag(ConventionalItemTags.RAW_FISHES_FOODS)
				.addOptionalTag(ConventionalItemTags.RAW_MEATS_FOODS)
				.addOptionalTag(ConventionalItemTags.FOOD_POISONING_FOODS)
				.add(Items.CHORUS_FRUIT)
				.add(Items.ENCHANTED_GOLDEN_APPLE)
				.add(Items.GOLDEN_APPLE)
				.addOptionalTag(tryParse("c:foods/doughs"))
				.addOptionalTag(tryParse("c:foods/pastas"))
				.addOptional(tryParse("farmersdelight:dog_food"))
				.addOptional(tryParse("farmersdelight:pie_crust"));
		getOrCreateTagBuilder(ModTags.Items.NO_LOYALTY)
				.addOptional(tryParse("impaled:pitchfork"));
		getOrCreateTagBuilder(ModTags.Items.RETAINS_DURABILITY)
				.addOptionalTag(tryParse("create:sandpaper"))
				.addOptional(tryParse("create:super_glue"));
		getOrCreateTagBuilder(ModTags.Items.WEAKLY_ENCHANTED)
				.addOptional(tryParse("impaled:pitchfork"));
	}
}
