/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import moriyashiine.enchancement.common.event.internal.InCombatEvent;
import moriyashiine.enchancement.common.init.EnchancementLootConditionTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class InCombatCondition implements LootItemCondition {
	public static final InCombatCondition INSTANCE = new InCombatCondition();
	public static final MapCodec<InCombatCondition> CODEC = MapCodec.unit(INSTANCE);

	private InCombatCondition() {
	}

	@Override
	public MapCodec<InCombatCondition> codec() {
		return EnchancementLootConditionTypes.IN_COMBAT;
	}

	@Override
	public boolean test(LootContext context) {
		Entity entity = context.getOptionalParameter(LootContextParams.THIS_ENTITY);
		if (entity == null) {
			return false;
		}
		return InCombatEvent.COMBAT_TICKS.getOrDefault(entity.getUUID(), 0) > 0;
	}
}
