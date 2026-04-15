/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public record ConditionalAttributeEnchantmentEffect(EnchantmentAttributeEffect effect, LootItemCondition condition) implements EnchantmentEntityEffect {
	public static final MapCodec<ConditionalAttributeEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					EnchantmentAttributeEffect.MAP_CODEC.fieldOf("attribute").forGetter(ConditionalAttributeEnchantmentEffect::effect),
					LootItemCondition.DIRECT_CODEC.fieldOf("condition").forGetter(ConditionalAttributeEnchantmentEffect::condition))
			.apply(instance, ConditionalAttributeEnchantmentEffect::new));

	@Override
	public MapCodec<ConditionalAttributeEnchantmentEffect> codec() {
		return CODEC;
	}

	@Override
	public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
		if (entity instanceof LivingEntity living) {
			LootContext context = createContext(serverLevel, living);
			if (condition().test(context)) {
				Identifier id = effect().id().withSuffix("/" + item.inSlot().getName());
				if (!living.getAttribute(effect().attribute()).hasModifier(id)) {
					effect().onChangedBlock(serverLevel, enchantmentLevel, item, entity, position, true);
					ModEntityComponents.CONDITIONAL_ATTRIBUTES.get(living).addAttribute(effect().attribute(), id, condition());
				}
			}
		}
	}

	public static LootContext createContext(ServerLevel level, LivingEntity living) {
		return new LootContext.Builder(new LootParams.Builder(level)
				.withParameter(LootContextParams.THIS_ENTITY, living)
				.withParameter(LootContextParams.ORIGIN, living.position())
				.withParameter(LootContextParams.DAMAGE_SOURCE, living.damageSources().magic())
				.create(LootContextParamSets.ENTITY)).create(Optional.empty());
	}
}
