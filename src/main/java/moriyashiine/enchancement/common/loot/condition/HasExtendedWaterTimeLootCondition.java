/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.loot.condition;

import com.mojang.serialization.MapCodec;
import moriyashiine.enchancement.common.component.entity.ExtendedWaterTimeComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModLootConditionTypes;
import net.minecraft.entity.Entity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;

public class HasExtendedWaterTimeLootCondition implements LootCondition {
	public static final HasExtendedWaterTimeLootCondition INSTANCE = new HasExtendedWaterTimeLootCondition();
	public static final MapCodec<HasExtendedWaterTimeLootCondition> CODEC = MapCodec.unit(INSTANCE);

	private HasExtendedWaterTimeLootCondition() {
	}

	@Override
	public LootConditionType getType() {
		return ModLootConditionTypes.HAS_EXTENDED_WATER_TIME;
	}

	@Override
	public boolean test(LootContext context) {
		Entity entity = context.get(LootContextParameters.THIS_ENTITY);
		if (entity == null) {
			return false;
		}
		ExtendedWaterTimeComponent extendedWaterTimeComponent = ModEntityComponents.EXTENDED_WATER_TIME.getNullable(entity);
		return extendedWaterTimeComponent != null && extendedWaterTimeComponent.getTicksWet() > 0;
	}
}
