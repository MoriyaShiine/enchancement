/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.config;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import moriyashiine.enchancement.common.world.level.storage.loot.functions.StoreItemEnchantmentsLootFunction;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.ArrayList;
import java.util.List;

public class OverhaulEnchantingEvent implements LootTableEvents.Modify {
	@Override
	public void modifyLootTable(ResourceKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, HolderLookup.Provider registries) {
		if (ModConfig.overhaulEnchanting != OverhaulMode.DISABLED) {
			if (key == BuiltInLootTables.NETHER_BRIDGE) {
				addChanceBook(tableBuilder, Items.IRON_HELMET);
			}
			if (key == BuiltInLootTables.TRIAL_CHAMBERS_REWARD_RARE) {
				addChanceBook(tableBuilder, Items.IRON_CHESTPLATE);
			}
			if (key == BuiltInLootTables.ANCIENT_CITY) {
				addChanceBook(tableBuilder, Items.IRON_LEGGINGS);
			}
			if (key == BuiltInLootTables.DESERT_PYRAMID) {
				addChanceBook(tableBuilder, Items.IRON_BOOTS);
			}
			if (key == BuiltInLootTables.SIMPLE_DUNGEON) {
				addGuaranteedBook(tableBuilder, Items.IRON_SWORD);
			}
			if (key == BuiltInLootTables.STRONGHOLD_CORRIDOR || key == BuiltInLootTables.STRONGHOLD_CROSSING) {
				addGuaranteedBook(tableBuilder, Items.IRON_SPEAR);
			}
			if (key == BuiltInLootTables.JUNGLE_TEMPLE) {
				addGuaranteedBook(tableBuilder, Items.BOW);
			}
			if (key == BuiltInLootTables.PILLAGER_OUTPOST) {
				addGuaranteedBook(tableBuilder, Items.CROSSBOW);
			}
			if (key == BuiltInLootTables.UNDERWATER_RUIN_BIG || key == BuiltInLootTables.UNDERWATER_RUIN_SMALL) {
				addChanceBook(tableBuilder, Items.TRIDENT);
			}
			if (key == BuiltInLootTables.RUINED_PORTAL) {
				addGuaranteedBook(tableBuilder, registries.lookupOrThrow(Registries.ENCHANTMENT), ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE);
			}
			if (key == BuiltInLootTables.ABANDONED_MINESHAFT) {
				addChanceBook(tableBuilder, Items.IRON_PICKAXE);
			}
			if (key == BuiltInLootTables.BASTION_OTHER) {
				addChanceBook(tableBuilder, Items.IRON_AXE);
			}
			if (key == BuiltInLootTables.BASTION_TREASURE) {
				addGuaranteedBook(tableBuilder, Items.IRON_AXE);
			}
			if (key == BuiltInLootTables.BURIED_TREASURE) {
				addGuaranteedBook(tableBuilder, Items.IRON_SHOVEL);
			}
			if (key == BuiltInLootTables.VILLAGE_DESERT_HOUSE || key == BuiltInLootTables.VILLAGE_PLAINS_HOUSE || key == BuiltInLootTables.VILLAGE_TAIGA_HOUSE || key == BuiltInLootTables.VILLAGE_SNOWY_HOUSE || key == BuiltInLootTables.VILLAGE_SAVANNA_HOUSE) {
				addChanceBook(tableBuilder, Items.IRON_HOE);
			}
			if (key == BuiltInLootTables.SHIPWRECK_TREASURE) {
				addGuaranteedBook(tableBuilder, Items.FISHING_ROD);
			}
		}
	}

	private static void addEnchantedBook(LootTable.Builder builder, NumberProvider rolls, Item item) {
		builder.withPool(LootPool.lootPool()
				.setRolls(rolls)
				.add(
						LootItem.lootTableItem(Items.BOOK)
								.setWeight(1)
								.apply(StoreItemEnchantmentsLootFunction.builder(item))
				));
	}

	private static void addChanceBook(LootTable.Builder builder, Item item) {
		addEnchantedBook(builder, UniformGenerator.between(0, 1), item);
	}

	private static void addGuaranteedBook(LootTable.Builder builder, Item item) {
		addEnchantedBook(builder, ConstantValue.exactly(1), item);
	}

	@SafeVarargs
	private static void addGuaranteedBook(LootTable.Builder builder, HolderLookup<Enchantment> enchantmentLookup, TagKey<Item>... tags) {
		List<Holder<Enchantment>> enchantments = new ArrayList<>();
		enchantmentLookup.listElements().forEach(enchantment -> enchantment.value().getSupportedItems().unwrapKey().ifPresent(tagKey -> {
			for (TagKey<Item> tag : tags) {
				if (tagKey == tag) {
					enchantments.add(enchantmentLookup.getOrThrow(enchantment.key()));
				}
			}
		}));
		builder.withPool(LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1))
				.add(
						LootItem.lootTableItem(
										Items.BOOK)
								.setWeight(1)
								.apply(new net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction.Builder().withOneOf(HolderSet.direct(enchantments)))
				));
	}
}
