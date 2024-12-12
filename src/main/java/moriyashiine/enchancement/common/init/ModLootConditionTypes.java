/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.lootcondition.AttackerBehindLootCondition;
import moriyashiine.enchancement.common.lootcondition.HasExtendedWaterTimeLootCondition;
import moriyashiine.enchancement.common.lootcondition.WetLootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModLootConditionTypes {
	public static final LootConditionType ATTACKER_BEHIND = new LootConditionType(AttackerBehindLootCondition.CODEC);
	public static final LootConditionType HAS_EXTENDED_WATER_TIME = new LootConditionType(HasExtendedWaterTimeLootCondition.CODEC);
	public static final LootConditionType WET = new LootConditionType(WetLootCondition.CODEC);

	public static void init() {
		Registry.register(Registries.LOOT_CONDITION_TYPE, Enchancement.id("attacker_behind"), ATTACKER_BEHIND);
		Registry.register(Registries.LOOT_CONDITION_TYPE, Enchancement.id("has_extended_water_time"), HAS_EXTENDED_WATER_TIME);
		Registry.register(Registries.LOOT_CONDITION_TYPE, Enchancement.id("wet"), WET);
	}
}
