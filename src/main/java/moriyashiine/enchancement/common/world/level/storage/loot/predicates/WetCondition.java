/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import moriyashiine.enchancement.common.init.ModLootConditionTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class WetCondition implements LootItemCondition {
	public static final WetCondition INSTANCE = new WetCondition();
	public static final MapCodec<WetCondition> CODEC = MapCodec.unit(INSTANCE);

	private WetCondition() {
	}

	@Override
	public MapCodec<WetCondition> codec() {
		return ModLootConditionTypes.WET;
	}

	@Override
	public boolean test(LootContext context) {
		Entity entity = context.getOptionalParameter(LootContextParams.THIS_ENTITY);
		if (entity == null) {
			return false;
		}
		return entity.isInWaterOrRain();
	}
}
