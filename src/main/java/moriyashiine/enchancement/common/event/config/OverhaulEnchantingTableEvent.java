/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.config;

import moriyashiine.enchancement.common.ModConfig;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.List;

public class OverhaulEnchantingTableEvent implements LootTableEvents.Modify {
	@Override
	public void modifyLootTable(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
		if (ModConfig.overhaulEnchantingTable.chiseledMode()) {
			RegistryWrapper<Enchantment> enchantmentLookup = registries.getOrThrow(RegistryKeys.ENCHANTMENT);
			if (key == LootTables.NETHER_BRIDGE_CHEST) {
				addGuaranteedBook(tableBuilder, enchantmentLookup, ItemTags.HEAD_ARMOR_ENCHANTABLE);
			}
			if (key == LootTables.TRIAL_CHAMBERS_REWARD_RARE_CHEST) {
				addGuaranteedBook(tableBuilder, enchantmentLookup, ItemTags.CHEST_ARMOR_ENCHANTABLE);
			}
			if (key == LootTables.ANCIENT_CITY_CHEST) {
				addGuaranteedBook(tableBuilder, enchantmentLookup, ItemTags.LEG_ARMOR_ENCHANTABLE);
			}
			if (key == LootTables.DESERT_PYRAMID_CHEST) {
				addGuaranteedBook(tableBuilder, enchantmentLookup, ItemTags.FOOT_ARMOR_ENCHANTABLE);
			}
			if (key == LootTables.SIMPLE_DUNGEON_CHEST) {
				addGuaranteedBook(tableBuilder, enchantmentLookup, ItemTags.SWORD_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE);
			}
			if (key == LootTables.JUNGLE_TEMPLE_CHEST) {
				addGuaranteedBook(tableBuilder, enchantmentLookup, ItemTags.BOW_ENCHANTABLE);
			}
			if (key == LootTables.PILLAGER_OUTPOST_CHEST) {
				addGuaranteedBook(tableBuilder, enchantmentLookup, ItemTags.CROSSBOW_ENCHANTABLE);
			}
			if (key == LootTables.UNDERWATER_RUIN_BIG_CHEST || key == LootTables.UNDERWATER_RUIN_SMALL_CHEST) {
				addGuaranteedBook(tableBuilder, enchantmentLookup, ItemTags.TRIDENT_ENCHANTABLE);
			}
			if (key == LootTables.RUINED_PORTAL_CHEST) {
				addGuaranteedBook(tableBuilder, enchantmentLookup, ItemTags.MINING_ENCHANTABLE);
			}
			if (key == LootTables.ABANDONED_MINESHAFT_CHEST) {
				addGuaranteedBook(tableBuilder, enchantmentLookup, ItemTags.PICKAXES);
			}
			if (key == LootTables.BASTION_OTHER_CHEST) {
				addChanceBook(tableBuilder, enchantmentLookup, ItemTags.AXES);
			}
			if (key == LootTables.BASTION_TREASURE_CHEST) {
				addGuaranteedBook(tableBuilder, enchantmentLookup, ItemTags.AXES);
			}
			if (key == LootTables.BURIED_TREASURE_CHEST) {
				addGuaranteedBook(tableBuilder, enchantmentLookup, ItemTags.SHOVELS);
			}
			if (key == LootTables.VILLAGE_DESERT_HOUSE_CHEST || key == LootTables.VILLAGE_PLAINS_CHEST || key == LootTables.VILLAGE_TAIGA_HOUSE_CHEST || key == LootTables.VILLAGE_SNOWY_HOUSE_CHEST || key == LootTables.VILLAGE_SAVANNA_HOUSE_CHEST) {
				addChanceBook(tableBuilder, enchantmentLookup, ItemTags.HOES);
			}
			if (key == LootTables.SHIPWRECK_TREASURE_CHEST) {
				addGuaranteedBook(tableBuilder, enchantmentLookup, ItemTags.FISHING_ENCHANTABLE);
			}
		}
	}

	@SafeVarargs
	private static void addEnchantedBook(LootTable.Builder builder, RegistryWrapper<Enchantment> enchantmentLookup, LootNumberProvider rolls, TagKey<Item>... tags) {
		List<RegistryEntry<Enchantment>> enchantments = new ArrayList<>();
		enchantmentLookup.streamEntries().forEach(enchantment -> enchantment.value().getApplicableItems().getTagKey().ifPresent(tagKey -> {
			for (TagKey<Item> tag : tags) {
				if (tagKey == tag) {
					enchantments.add(enchantmentLookup.getOrThrow(enchantment.registryKey()));
				}
			}
		}));
		builder.pool(LootPool.builder()
				.rolls(rolls)
				.with(
						ItemEntry.builder(
										Items.BOOK)
								.weight(1)
								.apply(new EnchantRandomlyLootFunction.Builder().options(RegistryEntryList.of(enchantments)))
				));
	}

	@SafeVarargs
	private static void addChanceBook(LootTable.Builder builder, RegistryWrapper<Enchantment> enchantmentLookup, TagKey<Item>... tags) {
		addEnchantedBook(builder, enchantmentLookup, UniformLootNumberProvider.create(0, 1), tags);
	}

	@SafeVarargs
	private static void addGuaranteedBook(LootTable.Builder builder, RegistryWrapper<Enchantment> enchantmentLookup, TagKey<Item>... tags) {
		addEnchantedBook(builder, enchantmentLookup, ConstantLootNumberProvider.create(1), tags);
	}
}
