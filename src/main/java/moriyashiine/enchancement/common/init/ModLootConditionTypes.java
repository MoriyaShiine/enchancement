/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.init;

import com.mojang.serialization.MapCodec;
import moriyashiine.enchancement.common.world.level.storage.loot.predicates.AttackerBehindCondition;
import moriyashiine.enchancement.common.world.level.storage.loot.predicates.HasExtendedWaterTimeCondition;
import moriyashiine.enchancement.common.world.level.storage.loot.predicates.InCombatCondition;
import moriyashiine.enchancement.common.world.level.storage.loot.predicates.WetCondition;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerLootConditionType;

public class ModLootConditionTypes {
	public static final MapCodec<AttackerBehindCondition> ATTACKER_BEHIND = registerLootConditionType("attacker_behind", AttackerBehindCondition.CODEC);
	public static final MapCodec<HasExtendedWaterTimeCondition> HAS_EXTENDED_WATER_TIME = registerLootConditionType("has_extended_water_time", HasExtendedWaterTimeCondition.CODEC);
	public static final MapCodec<InCombatCondition> IN_COMBAT = registerLootConditionType("in_combat", InCombatCondition.CODEC);
	public static final MapCodec<WetCondition> WET = registerLootConditionType("wet", WetCondition.CODEC);

	public static void init() {
	}
}
