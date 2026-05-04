/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.api.datagen.BaseBlocksProvider;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModBaseBlocksProvider extends BaseBlocksProvider {
	public ModBaseBlocksProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(packOutput, registriesFuture);
	}

	@Override
	protected void configure(BaseBlocksProvider.Output output) {
		output.accept(id("ditr:obisidan_diamond_ore"), Blocks.OBSIDIAN);

		output.accept(id("enderscape:mirestone_nebulite_ore"), id("enderscape:mirestone"));
		output.accept(id("enderscape:mirestone_shadoline_ore"), id("enderscape:mirestone"));
		output.accept(id("enderscape:nebulite_ore"), Blocks.END_STONE);
		output.accept(id("enderscape:shadoline_ore"), Blocks.END_STONE);

		output.accept(id("malum:blazing_quarz_ore"), Blocks.NETHERRACK);
		output.accept(id("malum:cthonic_gold_ore"), Blocks.DEEPSLATE);

		output.accept(id("natures_spirit:chert_coal_ore"), id("natures_spirit:chert"));
		output.accept(id("natures_spirit:chert_copper_ore"), id("natures_spirit:chert"));
		output.accept(id("natures_spirit:chert_diamond_ore"), id("natures_spirit:chert"));
		output.accept(id("natures_spirit:chert_emerald_ore"), id("natures_spirit:chert"));
		output.accept(id("natures_spirit:chert_gold_ore"), id("natures_spirit:chert"));
		output.accept(id("natures_spirit:chert_iron_ore"), id("natures_spirit:chert"));
		output.accept(id("natures_spirit:chert_lapis_ore"), id("natures_spirit:chert"));
		output.accept(id("natures_spirit:chert_redstone_ore"), id("natures_spirit:chert"));

		output.accept(id("spelunkery:calcite_redstone_ore"), Blocks.CALCITE);
		output.accept(id("spelunkery:sandstone_lapis_ore"), Blocks.SANDSTONE);
		output.accept(id("spelunkery:smooth_basalt_diamond_ore"), Blocks.SMOOTH_BASALT);
		output.accept(id("spelunkery:andesite_coal_ore"), Blocks.ANDESITE);
		output.accept(id("spelunkery:andesite_copper_ore"), Blocks.ANDESITE);
		output.accept(id("spelunkery:andesite_diamond_ore"), Blocks.ANDESITE);
		output.accept(id("spelunkery:andesite_emerald_ore"), Blocks.ANDESITE);
		output.accept(id("spelunkery:andesite_gold_ore"), Blocks.ANDESITE);
		output.accept(id("spelunkery:andesite_iron_ore"), Blocks.ANDESITE);
		output.accept(id("spelunkery:andesite_jade_ore"), Blocks.ANDESITE);
		output.accept(id("spelunkery:andesite_lapis_ore"), Blocks.ANDESITE);
		output.accept(id("spelunkery:andesite_lead_ore"), Blocks.ANDESITE);
		output.accept(id("spelunkery:andesite_redstone_ore"), Blocks.ANDESITE);
		output.accept(id("spelunkery:andesite_silver_ore"), Blocks.ANDESITE);
		output.accept(id("spelunkery:andesite_zinc_ore"), Blocks.ANDESITE);
		output.accept(id("spelunkery:diorite_coal_ore"), Blocks.DIORITE);
		output.accept(id("spelunkery:diorite_copper_ore"), Blocks.DIORITE);
		output.accept(id("spelunkery:diorite_diamond_ore"), Blocks.DIORITE);
		output.accept(id("spelunkery:diorite_emerald_ore"), Blocks.DIORITE);
		output.accept(id("spelunkery:diorite_gold_ore"), Blocks.DIORITE);
		output.accept(id("spelunkery:diorite_iron_ore"), Blocks.DIORITE);
		output.accept(id("spelunkery:diorite_jade_ore"), Blocks.DIORITE);
		output.accept(id("spelunkery:diorite_lapis_ore"), Blocks.DIORITE);
		output.accept(id("spelunkery:diorite_lead_ore"), Blocks.DIORITE);
		output.accept(id("spelunkery:diorite_redstone_ore"), Blocks.DIORITE);
		output.accept(id("spelunkery:diorite_silver_ore"), Blocks.DIORITE);
		output.accept(id("spelunkery:diorite_zinc_ore"), Blocks.DIORITE);
		output.accept(id("spelunkery:granite_coal_ore"), Blocks.GRANITE);
		output.accept(id("spelunkery:granite_copper_ore"), Blocks.GRANITE);
		output.accept(id("spelunkery:granite_diamond_ore"), Blocks.GRANITE);
		output.accept(id("spelunkery:granite_emerald_ore"), Blocks.GRANITE);
		output.accept(id("spelunkery:granite_gold_ore"), Blocks.GRANITE);
		output.accept(id("spelunkery:granite_iron_ore"), Blocks.GRANITE);
		output.accept(id("spelunkery:granite_jade_ore"), Blocks.GRANITE);
		output.accept(id("spelunkery:granite_lapis_ore"), Blocks.GRANITE);
		output.accept(id("spelunkery:granite_lead_ore"), Blocks.GRANITE);
		output.accept(id("spelunkery:granite_redstone_ore"), Blocks.GRANITE);
		output.accept(id("spelunkery:granite_silver_ore"), Blocks.GRANITE);
		output.accept(id("spelunkery:granite_zinc_ore"), Blocks.GRANITE);
		output.accept(id("spelunkery:tuff_coal_ore"), Blocks.TUFF);
		output.accept(id("spelunkery:tuff_copper_ore"), Blocks.TUFF);
		output.accept(id("spelunkery:tuff_diamond_ore"), Blocks.TUFF);
		output.accept(id("spelunkery:tuff_emerald_ore"), Blocks.TUFF);
		output.accept(id("spelunkery:tuff_gold_ore"), Blocks.TUFF);
		output.accept(id("spelunkery:tuff_iron_ore"), Blocks.TUFF);
		output.accept(id("spelunkery:tuff_jade_ore"), Blocks.TUFF);
		output.accept(id("spelunkery:tuff_lapis_ore"), Blocks.TUFF);
		output.accept(id("spelunkery:tuff_lead_ore"), Blocks.TUFF);
		output.accept(id("spelunkery:tuff_redstone_ore"), Blocks.TUFF);
		output.accept(id("spelunkery:tuff_silver_ore"), Blocks.TUFF);
		output.accept(id("spelunkery:tuff_zinc_ore"), Blocks.TUFF);

		output.accept(id("universal_ores:andesite_coal_ore"), Blocks.ANDESITE);
		output.accept(id("universal_ores:andesite_copper_ore"), Blocks.ANDESITE);
		output.accept(id("universal_ores:andesite_diamond_ore"), Blocks.ANDESITE);
		output.accept(id("universal_ores:andesite_emerald_ore"), Blocks.ANDESITE);
		output.accept(id("universal_ores:andesite_gold_ore"), Blocks.ANDESITE);
		output.accept(id("universal_ores:andesite_iron_ore"), Blocks.ANDESITE);
		output.accept(id("universal_ores:andesite_lapis_ore"), Blocks.ANDESITE);
		output.accept(id("universal_ores:andesite_redstone_ore"), Blocks.ANDESITE);
		output.accept(id("universal_ores:basalt_gold_ore"), Blocks.BASALT);
		output.accept(id("universal_ores:basalt_quartz_ore"), Blocks.BASALT);
		output.accept(id("universal_ores:blackstone_gold_ore"), Blocks.BLACKSTONE);
		output.accept(id("universal_ores:blackstone_quartz_ore"), Blocks.BLACKSTONE);
		output.accept(id("universal_ores:calcite_coal_ore"), Blocks.CALCITE);
		output.accept(id("universal_ores:calcite_copper_ore"), Blocks.CALCITE);
		output.accept(id("universal_ores:calcite_diamond_ore"), Blocks.CALCITE);
		output.accept(id("universal_ores:calcite_emerald_ore"), Blocks.CALCITE);
		output.accept(id("universal_ores:calcite_gold_ore"), Blocks.CALCITE);
		output.accept(id("universal_ores:calcite_iron_ore"), Blocks.CALCITE);
		output.accept(id("universal_ores:calcite_lapis_ore"), Blocks.CALCITE);
		output.accept(id("universal_ores:calcite_redstone_ore"), Blocks.CALCITE);
		output.accept(id("universal_ores:diorite_coal_ore"), Blocks.DIORITE);
		output.accept(id("universal_ores:diorite_copper_ore"), Blocks.DIORITE);
		output.accept(id("universal_ores:diorite_diamond_ore"), Blocks.DIORITE);
		output.accept(id("universal_ores:diorite_emerald_ore"), Blocks.DIORITE);
		output.accept(id("universal_ores:diorite_gold_ore"), Blocks.DIORITE);
		output.accept(id("universal_ores:diorite_iron_ore"), Blocks.DIORITE);
		output.accept(id("universal_ores:diorite_lapis_ore"), Blocks.DIORITE);
		output.accept(id("universal_ores:diorite_redstone_ore"), Blocks.DIORITE);
		output.accept(id("universal_ores:granite_coal_ore"), Blocks.GRANITE);
		output.accept(id("universal_ores:granite_copper_ore"), Blocks.GRANITE);
		output.accept(id("universal_ores:granite_diamond_ore"), Blocks.GRANITE);
		output.accept(id("universal_ores:granite_emerald_ore"), Blocks.GRANITE);
		output.accept(id("universal_ores:granite_gold_ore"), Blocks.GRANITE);
		output.accept(id("universal_ores:granite_iron_ore"), Blocks.GRANITE);
		output.accept(id("universal_ores:granite_lapis_ore"), Blocks.GRANITE);
		output.accept(id("universal_ores:granite_redstone_ore"), Blocks.GRANITE);
		output.accept(id("universal_ores:tuff_coal_ore"), Blocks.TUFF);
		output.accept(id("universal_ores:tuff_copper_ore"), Blocks.TUFF);
		output.accept(id("universal_ores:tuff_diamond_ore"), Blocks.TUFF);
		output.accept(id("universal_ores:tuff_emerald_ore"), Blocks.TUFF);
		output.accept(id("universal_ores:tuff_gold_ore"), Blocks.TUFF);
		output.accept(id("universal_ores:tuff_iron_ore"), Blocks.TUFF);
		output.accept(id("universal_ores:tuff_lapis_ore"), Blocks.TUFF);
		output.accept(id("universal_ores:tuff_redstone_ore"), Blocks.TUFF);
	}

	@Override
	public String getName() {
		return Enchancement.MOD_ID + "_base_blocks";
	}
}
