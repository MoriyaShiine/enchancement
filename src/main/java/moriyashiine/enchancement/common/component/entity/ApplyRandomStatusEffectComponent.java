/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.enchantment.effect.ApplyRandomStatusEffectEffect;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
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
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class ApplyRandomStatusEffectComponent implements Component {
	private ItemStack originalStack = ItemStack.EMPTY;

	@Override
	public void readData(ReadView readView) {
		originalStack = readView.read("OriginalStack", ItemStack.CODEC).orElse(ItemStack.EMPTY);
	}

	@Override
	public void writeData(WriteView writeView) {
		if (!originalStack.isEmpty()) {
			writeView.put("OriginalStack", ItemStack.CODEC, originalStack);
		}
	}

	public ItemStack getOriginalStack() {
		return originalStack;
	}

	public void setOriginalStack(ItemStack originalStack) {
		this.originalStack = originalStack;
	}

	public static void maybeSet(LivingEntity user, ItemStack arrowStack, float durationMultiplier, @Nullable ItemStack weaponStack, Consumer<List<StatusEffectInstance>> consumer) {
		MutableFloat duration = new MutableFloat();
		AtomicReference<TagKey<StatusEffect>> disallowedTag = new AtomicReference<>();
		if (weaponStack != null && EnchantmentHelper.hasAnyEnchantmentsWith(weaponStack, ModEnchantmentEffectComponentTypes.APPLY_RANDOM_STATUS_EFFECT)) {
			ApplyRandomStatusEffectEffect.setValues(user.getRandom(), duration, disallowedTag, Collections.singleton(weaponStack));
		} else if (!(user instanceof PlayerEntity) && EnchancementUtil.hasAnyEnchantmentsWith(user, ModEnchantmentEffectComponentTypes.APPLY_RANDOM_STATUS_EFFECT)) {
			ApplyRandomStatusEffectEffect.setValues(user.getRandom(), duration, disallowedTag, EnchancementUtil.getHeldItems(user));
		}
		if (duration.floatValue() != 0) {
			int attempts = 0;
			StatusEffectCategory category = user.isSneaking() ? StatusEffectCategory.BENEFICIAL : StatusEffectCategory.HARMFUL;
			PotionContentsComponent potionContentsComponent = arrowStack.getOrDefault(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Optional.empty(), Optional.empty(), new ArrayList<>(), Optional.empty()));
			Set<StatusEffect> disallowed = new HashSet<>();
			if (arrowStack.isOf(Items.SPECTRAL_ARROW)) {
				disallowed.add(StatusEffects.GLOWING.value());
			}
			for (StatusEffectInstance instance : potionContentsComponent.getEffects()) {
				disallowed.add(instance.getEffectType().value());
			}
			while (attempts < 128) {
				StatusEffect effect = Registries.STATUS_EFFECT.get(user.getRandom().nextInt(Registries.STATUS_EFFECT.size()));
				if (effect != null && !disallowed.contains(effect) && !effect.isInstant()) {
					Optional<RegistryKey<StatusEffect>> key = Registries.STATUS_EFFECT.getKey(effect);
					if (key.isPresent() && effect.getCategory() == category && !Registries.STATUS_EFFECT.getOrThrow(key.get()).isIn(disallowedTag.get())) {
						List<StatusEffectInstance> statusEffects = new ArrayList<>();
						for (StatusEffectInstance instance : potionContentsComponent.getEffects()) {
							statusEffects.add(new StatusEffectInstance(instance.getEffectType(), Math.max(instance.mapDuration(i -> i / 8), 1), instance.getAmplifier(), instance.isAmbient(), instance.shouldShowParticles()));
						}
						statusEffects.add(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(effect), MathHelper.floor(duration.floatValue() * durationMultiplier * 20)));
						consumer.accept(statusEffects);
						return;
					}
				}
				attempts++;
			}
		}
	}

	public static float getDurationMultiplier(LivingEntity user, float speed) {
		return MathHelper.lerp(Math.min(1, (speed * (user.isSneaking() ? 3 : 1)) / 3F), 0F, 1);
	}
}
