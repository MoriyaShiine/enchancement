/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.lootcondition;

import com.mojang.serialization.MapCodec;
import moriyashiine.enchancement.common.component.entity.InCombatComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModLootConditionTypes;
import net.minecraft.entity.Entity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;

public class InCombatLootCondition implements LootCondition {
	public static final InCombatLootCondition INSTANCE = new InCombatLootCondition();
	public static final MapCodec<InCombatLootCondition> CODEC = MapCodec.unit(INSTANCE);

	private InCombatLootCondition() {
	}

	@Override
	public LootConditionType getType() {
		return ModLootConditionTypes.IN_COMBAT;
	}

	@Override
	public boolean test(LootContext context) {
		Entity entity = context.get(LootContextParameters.THIS_ENTITY);
		if (entity == null) {
			return false;
		}
		InCombatComponent inCombatComponent = ModEntityComponents.IN_COMBAT.getNullable(entity);
		return inCombatComponent != null && inCombatComponent.inCombat();
	}
}
