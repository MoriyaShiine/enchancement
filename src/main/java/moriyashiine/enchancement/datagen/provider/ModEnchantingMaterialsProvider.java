/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.datagen.provider;

import moriyashiine.enchancement.api.datagen.EnchantingMaterialsProvider;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModEnchantingMaterialsProvider extends EnchantingMaterialsProvider {
	public ModEnchantingMaterialsProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(packOutput, registriesFuture);
	}

	@Override
	protected void configure(Output output) {
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
		output.accept(Items.SADDLE, ItemTags.IRON_TOOL_MATERIALS);
		output.accept(Items.SHEARS, ItemTags.IRON_TOOL_MATERIALS);
		output.accept(Items.SHIELD, ItemTags.IRON_TOOL_MATERIALS);
		output.accept(Items.TRIDENT, Items.PRISMARINE_SHARD);
		output.accept(Items.WARPED_FUNGUS_ON_A_STICK, Items.STRING);

		output.accept(id("enderscape:dagger"), key("c:ingots/shadoline"));
		output.accept(id("enderscape:magnia_attractor"), key("c:ingots/shadoline"));
		output.accept(id("enderscape:mirror"), key("c:gems/nebulite"));
	}

	@Override
	public String getName() {
		return Enchancement.MOD_ID + "_enchanting_materials";
	}
}
