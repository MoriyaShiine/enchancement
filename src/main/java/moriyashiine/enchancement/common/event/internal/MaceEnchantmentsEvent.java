/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.internal;

import moriyashiine.enchancement.common.init.ModEnchantments;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class MaceEnchantmentsEvent implements LootTableEvents.Modify {
	@Override
	public void modifyLootTable(ResourceKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, HolderLookup.Provider holder) {
		if (key == BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_RARE) {
			HolderLookup.RegistryLookup<Enchantment> enchantments = holder.lookupOrThrow(Registries.ENCHANTMENT);
			tableBuilder.modifyPools(builder -> {
				addEnchantedBook(builder, enchantments, ModEnchantments.METEOR);
				addEnchantedBook(builder, enchantments, ModEnchantments.THUNDERSTRUCK);
			});
		}
	}

	private static void addEnchantedBook(LootPool.Builder builder, HolderLookup.RegistryLookup<Enchantment> enchantments, ResourceKey<Enchantment> key) {
		enchantments.get(key).ifPresent(enchantment -> {
			if (!enchantment.is(ModEnchantments.EMPTY_KEY)) {
				builder.add(LootItem.lootTableItem(Items.BOOK)
						.setWeight(2)
						.apply(new SetEnchantmentsFunction.Builder().withEnchantment(enchantment, ConstantValue.exactly(1))));
			}
		});
	}
}
