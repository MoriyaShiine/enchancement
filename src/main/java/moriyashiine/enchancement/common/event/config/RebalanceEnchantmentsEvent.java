/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.config;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.advancements.criterion.DamageSourcePredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.effects.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RebalanceEnchantmentsEvent {
	public static class AllowEnchanting implements EnchantmentEvents.AllowEnchanting {
		@Override
		public TriState allowEnchanting(Holder<Enchantment> enchantment, ItemStack target, EnchantingContext enchantingContext) {
			if (ModConfig.rebalanceEnchantments && enchantment.is(Enchantments.FIRE_ASPECT)) {
				if (target.is(ItemTags.MACE_ENCHANTABLE)) {
					return TriState.FALSE;
				} else if (target.is(ItemTags.MINING_ENCHANTABLE)) {
					return TriState.TRUE;
				}
			}
			return TriState.DEFAULT;
		}
	}

	public static class ServerStarted implements ServerLifecycleEvents.ServerStarted {
		@SuppressWarnings("unchecked")
		@Override
		public void onServerStarted(MinecraftServer server) {
			if (ModConfig.rebalanceEnchantments) {
				{
					Enchantment channeling = server.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getValue(Enchantments.CHANNELING);
					if (channeling != null) {
						DataComponentMap.Builder builder = DataComponentMap.builder().addAll(channeling.effects());
						for (TypedDataComponent<?> component : channeling.effects()) {
							if (component.type() == EnchantmentEffectComponents.POST_ATTACK) {
								List<TargetedConditionalEffect<EnchantmentEntityEffect>> list = new ArrayList<>((List<TargetedConditionalEffect<EnchantmentEntityEffect>>) component.value());
								for (TargetedConditionalEffect<?> effect : list) {
									if (effect.requirements().isPresent() && effect.requirements().get() instanceof AllOfCondition allOf) {
										List<LootItemCondition> conditions = new ArrayList<>(allOf.terms);
										conditions.removeIf(term -> term.codec() == WeatherCheck.MAP_CODEC);
										effect.requirements = Optional.of(AllOfCondition.allOf(conditions));
									}
								}
								list.add(new TargetedConditionalEffect<>(
										EnchantmentTarget.ATTACKER,
										EnchantmentTarget.VICTIM,
										new Ignite(LevelBasedValue.perLevel(4.0F)),
										Optional.of(DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().isDirect(true)).build())
								));
								builder.set(EnchantmentEffectComponents.POST_ATTACK, list);
								continue;
							}
							if (component.type() == EnchantmentEffectComponents.HIT_BLOCK) {
								List<ConditionalEffect<EnchantmentEntityEffect>> list = (List<ConditionalEffect<EnchantmentEntityEffect>>) component.value();
								for (ConditionalEffect<?> effect : list) {
									if (effect.requirements().isPresent() && effect.requirements().get() instanceof AllOfCondition allOf) {
										List<LootItemCondition> conditions = new ArrayList<>(allOf.terms);
										conditions.removeIf(condition -> condition.codec() == WeatherCheck.MAP_CODEC);
										effect.requirements = Optional.of(AllOfCondition.allOf(conditions));
									}
								}
							}
							addEffect(component, builder);
						}
						builder.set(
								ModEnchantmentEffectComponentTypes.CHAIN_LIGHTNING,
								List.of(new ConditionalEffect<>(new AddValue(LevelBasedValue.perLevel(0.35F)), Optional.empty()))
						);
						channeling.effects = builder.build();
					}
				}
				{
					Enchantment fireAspect = server.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getValue(Enchantments.FIRE_ASPECT);
					if (fireAspect != null) {
						DataComponentMap.Builder builder = DataComponentMap.builder().addAll(fireAspect.effects());
						builder.set(
								ModEnchantmentEffectComponentTypes.SMELT_MINED_BLOCKS,
								Unit.INSTANCE
						);
						fireAspect.effects = builder.build();
					}
				}
				{
					Enchantment lunge = server.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getValue(Enchantments.LUNGE);
					if (lunge != null) {
						DataComponentMap.Builder builder = DataComponentMap.builder().addAll(lunge.effects());
						for (TypedDataComponent<?> component : lunge.effects()) {
							if (component.type() == EnchantmentEffectComponents.POST_PIERCING_ATTACK) {
								List<ConditionalEffect<EnchantmentEntityEffect>> list = (List<ConditionalEffect<EnchantmentEntityEffect>>) component.value();
								for (ConditionalEffect<?> effect : list) {
									if (effect.effect() instanceof AllOf.EntityEffects allOf) {
										List<EnchantmentEntityEffect> effects = new ArrayList<>(allOf.effects());
										effects.removeIf(e -> e.codec() == ApplyExhaustion.CODEC);
										allOf.effects = effects;
									}
									if (effect.requirements().isPresent() && effect.requirements().get() instanceof AllOfCondition allOf) {
										List<LootItemCondition> conditions = new ArrayList<>(allOf.terms);
										conditions.removeIf(condition -> condition.codec() == AnyOfCondition.MAP_CODEC);
										effect.requirements = Optional.of(AllOfCondition.allOf(conditions));
									}
								}
								builder.set(EnchantmentEffectComponents.POST_PIERCING_ATTACK, list);
								continue;
							}
							addEffect(component, builder);
						}
						lunge.effects = builder.build();
					}
				}
			}
		}

		private static <T> void addEffect(TypedDataComponent<T> component, DataComponentMap.Builder builder) {
			builder.set(component.type(), component.value());
		}
	}

	public static class UseBlock implements UseBlockCallback {
		@Override
		public InteractionResult interact(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
			if (ModConfig.rebalanceEnchantments && player.isShiftKeyDown() && hasIgnite(player.getItemInHand(hand))) {
				InteractionResult result = Items.FLINT_AND_STEEL.useOn(new UseOnContext(player, hand, hitResult));
				if (result != InteractionResult.FAIL) {
					return result;
				}
			}
			return InteractionResult.PASS;
		}

		private static boolean hasIgnite(ItemStack stack) {
			for (Holder<Enchantment> enchantment : stack.getEnchantments().keySet()) {
				for (TargetedConditionalEffect<EnchantmentEntityEffect> targetedEnchantmentEffect : enchantment.value().getEffects(EnchantmentEffectComponents.POST_ATTACK)) {
					if (targetedEnchantmentEffect.effect() instanceof Ignite) {
						return true;
					}
				}
			}
			return false;
		}
	}
}
