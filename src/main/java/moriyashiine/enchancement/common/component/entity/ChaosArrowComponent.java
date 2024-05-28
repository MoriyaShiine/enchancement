/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.tag.ModStatusEffectTags;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.*;
import java.util.function.Consumer;

public class ChaosArrowComponent implements Component {
	private ItemStack originalStack = ItemStack.EMPTY;

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		originalStack = ItemStack.fromNbt(registryLookup, tag.getCompound("OriginalStack")).orElse(ItemStack.EMPTY);
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		if (!originalStack.isEmpty()) {
			tag.put("OriginalStack", originalStack.encode(registryLookup));
		}
	}

	public ItemStack getOriginalStack() {
		return originalStack;
	}

	public void setOriginalStack(ItemStack originalStack) {
		this.originalStack = originalStack;
	}

	public static void applyChaos(LivingEntity shooter, ItemStack stack, Consumer<List<StatusEffectInstance>> consumer) {
		int level = shooter instanceof PlayerEntity ? EnchantmentHelper.getLevel(ModEnchantments.CHAOS, shooter.getActiveItem()) : EnchantmentHelper.getEquipmentLevel(ModEnchantments.CHAOS, shooter);
		if (level > 0) {
			int attempts = 0;
			StatusEffectCategory category = shooter.isSneaking() ? StatusEffectCategory.BENEFICIAL : StatusEffectCategory.HARMFUL;
			PotionContentsComponent potionContentsComponent = stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Optional.empty(), Optional.empty(), new ArrayList<>()));
			Set<StatusEffect> disallowed = new HashSet<>();
			if (stack.isOf(Items.SPECTRAL_ARROW)) {
				disallowed.add(StatusEffects.GLOWING.value());
			}
			for (StatusEffectInstance instance : potionContentsComponent.getEffects()) {
				disallowed.add(instance.getEffectType().value());
			}
			while (attempts < 128) {
				StatusEffect effect = Registries.STATUS_EFFECT.get(shooter.getRandom().nextInt(Registries.STATUS_EFFECT.size()));
				if (effect != null && !disallowed.contains(effect)) {
					Optional<RegistryKey<StatusEffect>> key = Registries.STATUS_EFFECT.getKey(effect);
					if (key.isPresent() && effect.getCategory() == category && !Registries.STATUS_EFFECT.entryOf(key.get()).isIn(ModStatusEffectTags.CHAOS_UNCHOOSABLE)) {
						List<StatusEffectInstance> statusEffects = new ArrayList<>();
						for (StatusEffectInstance instance : potionContentsComponent.getEffects()) {
							statusEffects.add(new StatusEffectInstance(instance.getEffectType(), Math.max(instance.mapDuration(i -> i / 8), 1), instance.getAmplifier(), instance.isAmbient(), instance.shouldShowParticles()));
						}
						statusEffects.add(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(effect), effect.isInstant() ? 1 : level * 100, effect.isInstant() ? level - 1 : 0));
						consumer.accept(statusEffects);
						return;
					}
				}
				attempts++;
			}
		}
	}
}
