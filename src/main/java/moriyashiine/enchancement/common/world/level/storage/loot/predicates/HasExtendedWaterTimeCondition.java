/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import moriyashiine.enchancement.common.component.entity.ExtendedWaterTimeComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModLootConditionTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class HasExtendedWaterTimeCondition implements LootItemCondition {
	public static final HasExtendedWaterTimeCondition INSTANCE = new HasExtendedWaterTimeCondition();
	public static final MapCodec<HasExtendedWaterTimeCondition> CODEC = MapCodec.unit(INSTANCE);

	private HasExtendedWaterTimeCondition() {
	}

	@Override
	public MapCodec<HasExtendedWaterTimeCondition> codec() {
		return ModLootConditionTypes.HAS_EXTENDED_WATER_TIME;
	}

	@Override
	public boolean test(LootContext context) {
		Entity entity = context.getOptionalParameter(LootContextParams.THIS_ENTITY);
		if (entity == null) {
			return false;
		}
		ExtendedWaterTimeComponent extendedWaterTimeComponent = ModEntityComponents.EXTENDED_WATER_TIME.getNullable(entity);
		return extendedWaterTimeComponent != null && extendedWaterTimeComponent.getTicksWet() > 0;
	}
}
