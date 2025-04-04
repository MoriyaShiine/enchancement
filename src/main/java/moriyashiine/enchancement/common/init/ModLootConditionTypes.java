/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.loot.condition.AttackerBehindLootCondition;
import moriyashiine.enchancement.common.loot.condition.HasExtendedWaterTimeLootCondition;
import moriyashiine.enchancement.common.loot.condition.InCombatLootCondition;
import moriyashiine.enchancement.common.loot.condition.WetLootCondition;
import net.minecraft.loot.condition.LootConditionType;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerLootConditionType;

public class ModLootConditionTypes {
	public static final LootConditionType ATTACKER_BEHIND = registerLootConditionType("attacker_behind", AttackerBehindLootCondition.CODEC);
	public static final LootConditionType HAS_EXTENDED_WATER_TIME = registerLootConditionType("has_extended_water_time", HasExtendedWaterTimeLootCondition.CODEC);
	public static final LootConditionType IN_COMBAT = registerLootConditionType("in_combat", InCombatLootCondition.CODEC);
	public static final LootConditionType WET = registerLootConditionType("wet", WetLootCondition.CODEC);

	public static void init() {
	}
}
