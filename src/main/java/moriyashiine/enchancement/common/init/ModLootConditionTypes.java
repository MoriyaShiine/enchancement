/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.lootcondition.AttackerBehindLootCondition;
import moriyashiine.enchancement.common.lootcondition.HasExtendedWaterTimeLootCondition;
import moriyashiine.enchancement.common.lootcondition.InCombatLootCondition;
import moriyashiine.enchancement.common.lootcondition.WetLootCondition;
import net.minecraft.loot.condition.LootConditionType;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerLootConditionType;

public class ModLootConditionTypes {
	public static final LootConditionType ATTACKER_BEHIND = registerLootConditionType("attacker_behind", new LootConditionType(AttackerBehindLootCondition.CODEC));
	public static final LootConditionType HAS_EXTENDED_WATER_TIME = registerLootConditionType("has_extended_water_time", new LootConditionType(HasExtendedWaterTimeLootCondition.CODEC));
	public static final LootConditionType IN_COMBAT = registerLootConditionType("in_combat", new LootConditionType(InCombatLootCondition.CODEC));
	public static final LootConditionType WET = registerLootConditionType("wet", new LootConditionType(WetLootCondition.CODEC));

	public static void init() {
	}
}
