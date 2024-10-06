/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.ModConfig;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.enchantment.effect.TargetedEnchantmentEffect;
import net.minecraft.enchantment.effect.entity.IgniteEnchantmentEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.MaceItem;
import net.minecraft.loot.condition.AllOfLootCondition;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionTypes;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RebalanceEnchantmentsEvent {
	public static class AllowEnchanting implements EnchantmentEvents.AllowEnchanting {
		@Override
		public TriState allowEnchanting(RegistryEntry<Enchantment> enchantment, ItemStack target, EnchantingContext enchantingContext) {
			if (ModConfig.rebalanceEnchantments && target.getItem() instanceof MaceItem && enchantment.matchesKey(Enchantments.FIRE_ASPECT)) {
				return TriState.FALSE;
			}
			return TriState.DEFAULT;
		}
	}

	public static class ServerStarted implements ServerLifecycleEvents.ServerStarted {
		@SuppressWarnings("unchecked")
		@Override
		public void onServerStarted(MinecraftServer server) {
			if (ModConfig.rebalanceEnchantments) {
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

	public static class UseBlock implements UseBlockCallback {
		@Override
		public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
			if (ModConfig.rebalanceEnchantments && player.isSneaking() && hasIgnite(player.getStackInHand(hand))) {
				ActionResult result = Items.FLINT_AND_STEEL.useOnBlock(new ItemUsageContext(player, hand, hitResult));
				if (result != ActionResult.FAIL) {
					return result;
				}
			}
			return ActionResult.PASS;
		}

		private static boolean hasIgnite(ItemStack stack) {
			for (RegistryEntry<Enchantment> enchantment : EnchantmentHelper.getEnchantments(stack).getEnchantments()) {
				for (TargetedEnchantmentEffect<EnchantmentEntityEffect> targetedEnchantmentEffect : enchantment.value().getEffect(EnchantmentEffectComponentTypes.POST_ATTACK)) {
					if (targetedEnchantmentEffect.effect() instanceof IgniteEnchantmentEffect) {
						return true;
					}
				}
			}
			return false;
		}
	}
}
