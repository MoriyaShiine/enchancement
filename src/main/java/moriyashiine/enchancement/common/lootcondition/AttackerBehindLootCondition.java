/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.lootcondition;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import moriyashiine.enchancement.common.init.ModLootConditionTypes;
import net.minecraft.entity.Entity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.context.ContextParameter;
import net.minecraft.util.math.MathHelper;

import java.util.Set;

public class AttackerBehindLootCondition implements LootCondition {
	public static final AttackerBehindLootCondition INSTANCE = new AttackerBehindLootCondition();
	public static final MapCodec<AttackerBehindLootCondition> CODEC = MapCodec.unit(INSTANCE);

	private AttackerBehindLootCondition() {
	}

	@Override
	public LootConditionType getType() {
		return ModLootConditionTypes.ATTACKER_BEHIND;
	}

	@Override
	public boolean test(LootContext context) {
		Entity entity = context.get(LootContextParameters.THIS_ENTITY);
		Entity attackingEntity = context.get(LootContextParameters.ATTACKING_ENTITY);
		if (entity == null || attackingEntity == null) {
			return false;
		}
		return Math.abs(MathHelper.subtractAngles(entity.getHeadYaw(), attackingEntity.getHeadYaw())) <= 75;
	}

	@Override
	public Set<ContextParameter<?>> getAllowedParameters() {
		return ImmutableSet.of(LootContextParameters.THIS_ENTITY, LootContextParameters.ATTACKING_ENTITY);
	}
}
