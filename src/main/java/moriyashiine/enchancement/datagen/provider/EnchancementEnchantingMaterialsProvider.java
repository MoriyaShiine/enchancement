/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.datagen.provider;

import moriyashiine.enchancement.api.datagen.EnchantingMaterialsProvider;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.references.BlockItemIds;
import net.minecraft.references.ItemIds;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public class EnchancementEnchantingMaterialsProvider extends EnchantingMaterialsProvider {
	public EnchancementEnchantingMaterialsProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(packOutput, registriesFuture);
	}

	@Override
	protected void configure(Output output) {
		output.accept(ItemIds.BOW, BlockItemIds.TRIPWIRE.item());
		output.accept(ItemIds.BRUSH, ItemTags.COPPER_TOOL_MATERIALS);
		output.accept(ItemIds.CARROT_ON_A_STICK, BlockItemIds.TRIPWIRE.item());
		output.accept(ItemIds.COPPER_HORSE_ARMOR, ItemTags.REPAIRS_COPPER_ARMOR);
		output.accept(ItemIds.COPPER_NAUTILUS_ARMOR, ItemTags.REPAIRS_COPPER_ARMOR);
		output.accept(ItemIds.CROSSBOW, BlockItemIds.TRIPWIRE.item());
		output.accept(ItemIds.DIAMOND_HORSE_ARMOR, ItemTags.REPAIRS_DIAMOND_ARMOR);
		output.accept(ItemIds.DIAMOND_NAUTILUS_ARMOR, ItemTags.REPAIRS_DIAMOND_ARMOR);
		output.accept(ItemIds.FISHING_ROD, BlockItemIds.TRIPWIRE.item());
		output.accept(ItemIds.FLINT_AND_STEEL, ItemTags.IRON_TOOL_MATERIALS);
		output.accept(ItemIds.GOLDEN_HORSE_ARMOR, ItemTags.REPAIRS_GOLD_ARMOR);
		output.accept(ItemIds.GOLDEN_NAUTILUS_ARMOR, ItemTags.REPAIRS_GOLD_ARMOR);
		output.accept(ItemIds.IRON_HORSE_ARMOR, ItemTags.REPAIRS_IRON_ARMOR);
		output.accept(ItemIds.IRON_NAUTILUS_ARMOR, ItemTags.REPAIRS_IRON_ARMOR);
		output.accept(ItemIds.LEATHER_HORSE_ARMOR, ItemTags.REPAIRS_LEATHER_ARMOR);
		output.accept(ItemIds.NETHERITE_HORSE_ARMOR, ItemTags.REPAIRS_DIAMOND_ARMOR);
		output.accept(ItemIds.NETHERITE_NAUTILUS_ARMOR, ItemTags.REPAIRS_DIAMOND_ARMOR);
		output.accept(ItemIds.SADDLE, ItemTags.IRON_TOOL_MATERIALS);
		output.accept(ItemIds.SHEARS, ItemTags.IRON_TOOL_MATERIALS);
		output.accept(ItemIds.SHIELD, ItemTags.IRON_TOOL_MATERIALS);
		output.accept(ItemIds.TRIDENT, ItemIds.PRISMARINE_SHARD);
		output.accept(ItemIds.WARPED_FUNGUS_ON_A_STICK, BlockItemIds.TRIPWIRE.item());

		output.accept(key("enderscape:dagger"), tagKey("c:ingots/shadoline"));
		output.accept(key("enderscape:magnia_attractor"), tagKey("c:ingots/shadoline"));
		output.accept(key("enderscape:mirror"), tagKey("c:gems/nebulite"));
	}

	@Override
	public String getName() {
		return Enchancement.MOD_ID + "_enchanting_materials";
	}
}
