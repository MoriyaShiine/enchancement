/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.datagen.provider;

import moriyashiine.enchancement.api.datagen.BaseBlocksProvider;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.references.BlockItemIds;

import java.util.concurrent.CompletableFuture;

public class EnchancementBaseBlocksProvider extends BaseBlocksProvider {
	public EnchancementBaseBlocksProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(packOutput, registriesFuture);
	}

	@Override
	protected void configure(BaseBlocksProvider.Output output) {
		output.accept(key("ditr:obisidan_diamond_ore"), BlockItemIds.OBSIDIAN);

		output.accept(key("enderscape:mirestone_nebulite_ore"), key("enderscape:mirestone"));
		output.accept(key("enderscape:mirestone_shadoline_ore"), key("enderscape:mirestone"));
		output.accept(key("enderscape:nebulite_ore"), BlockItemIds.END_STONE);
		output.accept(key("enderscape:shadoline_ore"), BlockItemIds.END_STONE);

		output.accept(key("malum:blazing_quarz_ore"), BlockItemIds.NETHERRACK);
		output.accept(key("malum:cthonic_gold_ore"), BlockItemIds.DEEPSLATE);

		output.accept(key("natures_spirit:chert_coal_ore"), key("natures_spirit:chert"));
		output.accept(key("natures_spirit:chert_copper_ore"), key("natures_spirit:chert"));
		output.accept(key("natures_spirit:chert_diamond_ore"), key("natures_spirit:chert"));
		output.accept(key("natures_spirit:chert_emerald_ore"), key("natures_spirit:chert"));
		output.accept(key("natures_spirit:chert_gold_ore"), key("natures_spirit:chert"));
		output.accept(key("natures_spirit:chert_iron_ore"), key("natures_spirit:chert"));
		output.accept(key("natures_spirit:chert_lapis_ore"), key("natures_spirit:chert"));
		output.accept(key("natures_spirit:chert_redstone_ore"), key("natures_spirit:chert"));

		output.accept(key("spelunkery:calcite_redstone_ore"), BlockItemIds.CALCITE);
		output.accept(key("spelunkery:sandstone_lapis_ore"), BlockItemIds.SANDSTONE);
		output.accept(key("spelunkery:smooth_basalt_diamond_ore"), BlockItemIds.SMOOTH_BASALT);
		output.accept(key("spelunkery:andesite_coal_ore"), BlockItemIds.ANDESITE);
		output.accept(key("spelunkery:andesite_copper_ore"), BlockItemIds.ANDESITE);
		output.accept(key("spelunkery:andesite_diamond_ore"), BlockItemIds.ANDESITE);
		output.accept(key("spelunkery:andesite_emerald_ore"), BlockItemIds.ANDESITE);
		output.accept(key("spelunkery:andesite_gold_ore"), BlockItemIds.ANDESITE);
		output.accept(key("spelunkery:andesite_iron_ore"), BlockItemIds.ANDESITE);
		output.accept(key("spelunkery:andesite_jade_ore"), BlockItemIds.ANDESITE);
		output.accept(key("spelunkery:andesite_lapis_ore"), BlockItemIds.ANDESITE);
		output.accept(key("spelunkery:andesite_lead_ore"), BlockItemIds.ANDESITE);
		output.accept(key("spelunkery:andesite_redstone_ore"), BlockItemIds.ANDESITE);
		output.accept(key("spelunkery:andesite_silver_ore"), BlockItemIds.ANDESITE);
		output.accept(key("spelunkery:andesite_zinc_ore"), BlockItemIds.ANDESITE);
		output.accept(key("spelunkery:diorite_coal_ore"), BlockItemIds.DIORITE);
		output.accept(key("spelunkery:diorite_copper_ore"), BlockItemIds.DIORITE);
		output.accept(key("spelunkery:diorite_diamond_ore"), BlockItemIds.DIORITE);
		output.accept(key("spelunkery:diorite_emerald_ore"), BlockItemIds.DIORITE);
		output.accept(key("spelunkery:diorite_gold_ore"), BlockItemIds.DIORITE);
		output.accept(key("spelunkery:diorite_iron_ore"), BlockItemIds.DIORITE);
		output.accept(key("spelunkery:diorite_jade_ore"), BlockItemIds.DIORITE);
		output.accept(key("spelunkery:diorite_lapis_ore"), BlockItemIds.DIORITE);
		output.accept(key("spelunkery:diorite_lead_ore"), BlockItemIds.DIORITE);
		output.accept(key("spelunkery:diorite_redstone_ore"), BlockItemIds.DIORITE);
		output.accept(key("spelunkery:diorite_silver_ore"), BlockItemIds.DIORITE);
		output.accept(key("spelunkery:diorite_zinc_ore"), BlockItemIds.DIORITE);
		output.accept(key("spelunkery:granite_coal_ore"), BlockItemIds.GRANITE);
		output.accept(key("spelunkery:granite_copper_ore"), BlockItemIds.GRANITE);
		output.accept(key("spelunkery:granite_diamond_ore"), BlockItemIds.GRANITE);
		output.accept(key("spelunkery:granite_emerald_ore"), BlockItemIds.GRANITE);
		output.accept(key("spelunkery:granite_gold_ore"), BlockItemIds.GRANITE);
		output.accept(key("spelunkery:granite_iron_ore"), BlockItemIds.GRANITE);
		output.accept(key("spelunkery:granite_jade_ore"), BlockItemIds.GRANITE);
		output.accept(key("spelunkery:granite_lapis_ore"), BlockItemIds.GRANITE);
		output.accept(key("spelunkery:granite_lead_ore"), BlockItemIds.GRANITE);
		output.accept(key("spelunkery:granite_redstone_ore"), BlockItemIds.GRANITE);
		output.accept(key("spelunkery:granite_silver_ore"), BlockItemIds.GRANITE);
		output.accept(key("spelunkery:granite_zinc_ore"), BlockItemIds.GRANITE);
		output.accept(key("spelunkery:tuff_coal_ore"), BlockItemIds.TUFF);
		output.accept(key("spelunkery:tuff_copper_ore"), BlockItemIds.TUFF);
		output.accept(key("spelunkery:tuff_diamond_ore"), BlockItemIds.TUFF);
		output.accept(key("spelunkery:tuff_emerald_ore"), BlockItemIds.TUFF);
		output.accept(key("spelunkery:tuff_gold_ore"), BlockItemIds.TUFF);
		output.accept(key("spelunkery:tuff_iron_ore"), BlockItemIds.TUFF);
		output.accept(key("spelunkery:tuff_jade_ore"), BlockItemIds.TUFF);
		output.accept(key("spelunkery:tuff_lapis_ore"), BlockItemIds.TUFF);
		output.accept(key("spelunkery:tuff_lead_ore"), BlockItemIds.TUFF);
		output.accept(key("spelunkery:tuff_redstone_ore"), BlockItemIds.TUFF);
		output.accept(key("spelunkery:tuff_silver_ore"), BlockItemIds.TUFF);
		output.accept(key("spelunkery:tuff_zinc_ore"), BlockItemIds.TUFF);

		output.accept(key("universal_ores:andesite_coal_ore"), BlockItemIds.ANDESITE);
		output.accept(key("universal_ores:andesite_copper_ore"), BlockItemIds.ANDESITE);
		output.accept(key("universal_ores:andesite_diamond_ore"), BlockItemIds.ANDESITE);
		output.accept(key("universal_ores:andesite_emerald_ore"), BlockItemIds.ANDESITE);
		output.accept(key("universal_ores:andesite_gold_ore"), BlockItemIds.ANDESITE);
		output.accept(key("universal_ores:andesite_iron_ore"), BlockItemIds.ANDESITE);
		output.accept(key("universal_ores:andesite_lapis_ore"), BlockItemIds.ANDESITE);
		output.accept(key("universal_ores:andesite_redstone_ore"), BlockItemIds.ANDESITE);
		output.accept(key("universal_ores:basalt_gold_ore"), BlockItemIds.BASALT);
		output.accept(key("universal_ores:basalt_quartz_ore"), BlockItemIds.BASALT);
		output.accept(key("universal_ores:blackstone_gold_ore"), BlockItemIds.BLACKSTONE);
		output.accept(key("universal_ores:blackstone_quartz_ore"), BlockItemIds.BLACKSTONE);
		output.accept(key("universal_ores:calcite_coal_ore"), BlockItemIds.CALCITE);
		output.accept(key("universal_ores:calcite_copper_ore"), BlockItemIds.CALCITE);
		output.accept(key("universal_ores:calcite_diamond_ore"), BlockItemIds.CALCITE);
		output.accept(key("universal_ores:calcite_emerald_ore"), BlockItemIds.CALCITE);
		output.accept(key("universal_ores:calcite_gold_ore"), BlockItemIds.CALCITE);
		output.accept(key("universal_ores:calcite_iron_ore"), BlockItemIds.CALCITE);
		output.accept(key("universal_ores:calcite_lapis_ore"), BlockItemIds.CALCITE);
		output.accept(key("universal_ores:calcite_redstone_ore"), BlockItemIds.CALCITE);
		output.accept(key("universal_ores:diorite_coal_ore"), BlockItemIds.DIORITE);
		output.accept(key("universal_ores:diorite_copper_ore"), BlockItemIds.DIORITE);
		output.accept(key("universal_ores:diorite_diamond_ore"), BlockItemIds.DIORITE);
		output.accept(key("universal_ores:diorite_emerald_ore"), BlockItemIds.DIORITE);
		output.accept(key("universal_ores:diorite_gold_ore"), BlockItemIds.DIORITE);
		output.accept(key("universal_ores:diorite_iron_ore"), BlockItemIds.DIORITE);
		output.accept(key("universal_ores:diorite_lapis_ore"), BlockItemIds.DIORITE);
		output.accept(key("universal_ores:diorite_redstone_ore"), BlockItemIds.DIORITE);
		output.accept(key("universal_ores:granite_coal_ore"), BlockItemIds.GRANITE);
		output.accept(key("universal_ores:granite_copper_ore"), BlockItemIds.GRANITE);
		output.accept(key("universal_ores:granite_diamond_ore"), BlockItemIds.GRANITE);
		output.accept(key("universal_ores:granite_emerald_ore"), BlockItemIds.GRANITE);
		output.accept(key("universal_ores:granite_gold_ore"), BlockItemIds.GRANITE);
		output.accept(key("universal_ores:granite_iron_ore"), BlockItemIds.GRANITE);
		output.accept(key("universal_ores:granite_lapis_ore"), BlockItemIds.GRANITE);
		output.accept(key("universal_ores:granite_redstone_ore"), BlockItemIds.GRANITE);
		output.accept(key("universal_ores:tuff_coal_ore"), BlockItemIds.TUFF);
		output.accept(key("universal_ores:tuff_copper_ore"), BlockItemIds.TUFF);
		output.accept(key("universal_ores:tuff_diamond_ore"), BlockItemIds.TUFF);
		output.accept(key("universal_ores:tuff_emerald_ore"), BlockItemIds.TUFF);
		output.accept(key("universal_ores:tuff_gold_ore"), BlockItemIds.TUFF);
		output.accept(key("universal_ores:tuff_iron_ore"), BlockItemIds.TUFF);
		output.accept(key("universal_ores:tuff_lapis_ore"), BlockItemIds.TUFF);
		output.accept(key("universal_ores:tuff_redstone_ore"), BlockItemIds.TUFF);
	}

	@Override
	public String getName() {
		return Enchancement.MOD_ID + "_base_blocks";
	}
}
