/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.lootcondition;

import com.mojang.serialization.MapCodec;
import moriyashiine.enchancement.common.init.ModLootConditionTypes;
import net.minecraft.entity.Entity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;

public class WetLootCondition implements LootCondition {
	public static final WetLootCondition INSTANCE = new WetLootCondition();
	public static final MapCodec<WetLootCondition> CODEC = MapCodec.unit(INSTANCE);

	private WetLootCondition() {
	}

	@Override
	public LootConditionType getType() {
		return ModLootConditionTypes.WET;
	}

	@Override
	public boolean test(LootContext context) {
		Entity entity = context.get(LootContextParameters.THIS_ENTITY);
		if (entity == null) {
			return false;
		}
		return entity.isTouchingWaterOrRain();
	}
}
