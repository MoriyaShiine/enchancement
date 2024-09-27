/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.ModConfig;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.enchantment.effect.TargetedEnchantmentEffect;
import net.minecraft.enchantment.effect.entity.IgniteEnchantmentEffect;
import net.minecraft.loot.condition.AllOfLootCondition;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionTypes;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RebalanceChannelingEvent implements ServerLifecycleEvents.ServerStarted {
	@SuppressWarnings("unchecked")
	@Override
	public void onServerStarted(MinecraftServer server) {
		if (ModConfig.rebalanceChanneling) {
			Enchantment channeling = server.getRegistryManager().get(RegistryKeys.ENCHANTMENT).get(Enchantments.CHANNELING);
			if (channeling != null) {
				ComponentMap.Builder builder = ComponentMap.builder().addAll(channeling.effects());
				for (Component<?> effect : channeling.effects()) {
					if (effect.type() == EnchantmentEffectComponentTypes.POST_ATTACK) {
						List<TargetedEnchantmentEffect<EnchantmentEntityEffect>> list = new ArrayList<>((List<TargetedEnchantmentEffect<EnchantmentEntityEffect>>) effect.value());
						for (TargetedEnchantmentEffect<?> targetedEnchantmentEffect : list) {
							if (targetedEnchantmentEffect.requirements().isPresent() && targetedEnchantmentEffect.requirements().get() instanceof AllOfLootCondition all) {
								List<LootCondition> terms = new ArrayList<>(all.terms);
								terms.removeIf(term -> term.getType() == LootConditionTypes.WEATHER_CHECK);
								targetedEnchantmentEffect.requirements = Optional.of(AllOfLootCondition.create(terms));
							}
						}
						list.add(new TargetedEnchantmentEffect<>(
								EnchantmentEffectTarget.ATTACKER,
								EnchantmentEffectTarget.VICTIM,
								new IgniteEnchantmentEffect(EnchantmentLevelBasedValue.linear(4.0F)),
								Optional.of(DamageSourcePropertiesLootCondition.builder(DamageSourcePredicate.Builder.create().isDirect(true)).build())
						));
						builder.add(EnchantmentEffectComponentTypes.POST_ATTACK, list);
						continue;
					}
					if (effect.type() == EnchantmentEffectComponentTypes.HIT_BLOCK) {
						List<EnchantmentEffectEntry<EnchantmentEntityEffect>> list = (List<EnchantmentEffectEntry<EnchantmentEntityEffect>>) effect.value();
						for (EnchantmentEffectEntry<?> effectEntry : list) {
							if (effectEntry.requirements().isPresent() && effectEntry.requirements().get() instanceof AllOfLootCondition all) {
								List<LootCondition> terms = new ArrayList<>(all.terms);
								terms.removeIf(term -> term.getType() == LootConditionTypes.WEATHER_CHECK);
								effectEntry.requirements = Optional.of(AllOfLootCondition.create(terms));
							}
						}
					}
					addEffect(effect, builder);
				}
				channeling.effects = builder.build();
			}
		}
	}

	private static <T> void addEffect(Component<T> component, ComponentMap.Builder builder) {
		builder.add(component.type(), component.value());
	}
}
