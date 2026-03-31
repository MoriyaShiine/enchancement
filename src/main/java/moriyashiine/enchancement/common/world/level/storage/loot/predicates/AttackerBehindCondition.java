/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.level.storage.loot.predicates;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import moriyashiine.enchancement.common.init.ModLootConditionTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Set;

public class AttackerBehindCondition implements LootItemCondition {
	public static final AttackerBehindCondition INSTANCE = new AttackerBehindCondition();
	public static final MapCodec<AttackerBehindCondition> CODEC = MapCodec.unit(INSTANCE);

	private AttackerBehindCondition() {
	}

	@Override
	public MapCodec<AttackerBehindCondition> codec() {
		return ModLootConditionTypes.ATTACKER_BEHIND;
	}

	@Override
	public boolean test(LootContext context) {
		Entity entity = context.getOptionalParameter(LootContextParams.THIS_ENTITY);
		Entity attackingEntity = context.getOptionalParameter(LootContextParams.ATTACKING_ENTITY);
		if (entity == null || attackingEntity == null) {
			return false;
		}
		return Math.abs(Mth.degreesDifference(entity.getYHeadRot(), attackingEntity.getYHeadRot())) <= 75;
	}

	@Override
	public Set<ContextKey<?>> getReferencedContextParams() {
		return ImmutableSet.of(LootContextParams.THIS_ENTITY, LootContextParams.ATTACKING_ENTITY);
	}
}
