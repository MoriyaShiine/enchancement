/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.world.item.effects.ApplyRandomMobEffectEffect;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jspecify.annotations.Nullable;
import org.ladysnake.cca.api.v8.component.CardinalComponent;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class ApplyRandomMobEffectComponent implements CardinalComponent {
	private ItemStack originalStack = ItemStack.EMPTY;

	@Override
	public void readData(ValueInput input) {
		originalStack = input.read("OriginalStack", ItemStack.CODEC).orElse(ItemStack.EMPTY);
	}

	@Override
	public void writeData(ValueOutput output) {
		if (!originalStack.isEmpty()) {
			output.store("OriginalStack", ItemStack.CODEC, originalStack);
		}
	}

	public ItemStack getOriginalStack() {
		return originalStack;
	}

	public void setOriginalStack(ItemStack originalStack) {
		this.originalStack = originalStack;
	}

	public static void maybeSet(LivingEntity user, ItemStack projectile, double durationMultiplier, @Nullable ItemStack weapon, Consumer<List<MobEffectInstance>> consumer) {
		MutableFloat duration = new MutableFloat();
		AtomicReference<TagKey<MobEffect>> disallowedTag = new AtomicReference<>();
		if (weapon != null && EnchantmentHelper.has(weapon, ModEnchantmentEffectComponentTypes.APPLY_RANDOM_MOB_EFFECT)) {
			ApplyRandomMobEffectEffect.setValues(user.getRandom(), duration, disallowedTag, Collections.singleton(weapon));
		} else if (!(user instanceof Player) && EnchancementUtil.hasAnyEnchantmentsWith(user, ModEnchantmentEffectComponentTypes.APPLY_RANDOM_MOB_EFFECT)) {
			ApplyRandomMobEffectEffect.setValues(user.getRandom(), duration, disallowedTag, EnchancementUtil.getHeldItems(user));
		}
		if (duration.floatValue() != 0) {
			int attempts = 0;
			MobEffectCategory category = user.isShiftKeyDown() ? MobEffectCategory.BENEFICIAL : MobEffectCategory.HARMFUL;
			PotionContents potionContents = projectile.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
			Set<MobEffect> disallowed = new HashSet<>();
			if (projectile.is(Items.SPECTRAL_ARROW)) {
				disallowed.add(MobEffects.GLOWING.value());
			}
			for (MobEffectInstance instance : potionContents.getAllEffects()) {
				disallowed.add(instance.getEffect().value());
			}
			while (attempts < 128) {
				MobEffect effect = BuiltInRegistries.MOB_EFFECT.byId(user.getRandom().nextInt(BuiltInRegistries.MOB_EFFECT.size()));
				if (effect != null && !disallowed.contains(effect) && !effect.isInstantenous()) {
					Optional<ResourceKey<MobEffect>> key = BuiltInRegistries.MOB_EFFECT.getResourceKey(effect);
					if (key.isPresent() && effect.getCategory() == category && !BuiltInRegistries.MOB_EFFECT.getOrThrow(key.get()).is(disallowedTag.get())) {
						List<MobEffectInstance> effects = new ArrayList<>();
						for (MobEffectInstance instance : potionContents.getAllEffects()) {
							effects.add(new MobEffectInstance(instance.getEffect(), Math.max(instance.mapDuration(i -> i / 8), 1), instance.getAmplifier(), instance.isAmbient(), instance.isVisible()));
						}
						effects.add(new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect), Mth.floor(duration.floatValue() * durationMultiplier * 20)));
						consumer.accept(effects);
						return;
					}
				}
				attempts++;
			}
		}
	}

	public static double getDurationMultiplier(LivingEntity user, double speed) {
		return Mth.lerp(Math.min(1, (speed * (user.isShiftKeyDown() ? 3 : 1)) / 3F), 0, 1);
	}
}
