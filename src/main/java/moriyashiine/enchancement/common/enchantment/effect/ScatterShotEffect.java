/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.enchantment.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record ScatterShotEffect(EnchantmentValueEffect minimum, EnchantmentValueEffect maximum,
								Item allowedProjectile) {
	public static final Codec<ScatterShotEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("minimum").forGetter(ScatterShotEffect::minimum),
					EnchantmentValueEffect.CODEC.fieldOf("maximum").forGetter(ScatterShotEffect::maximum),
					Registries.ITEM.getCodec().fieldOf("allowed_projectile").forGetter(ScatterShotEffect::allowedProjectile))
			.apply(instance, ScatterShotEffect::new));

	public static boolean hasScatterShot = false;

	public static int getMinimum(Random random, ItemStack stack) {
		MutableFloat minimum = new MutableFloat(0);
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			List<EnchantmentEffectEntry<ScatterShotEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.SCATTER_SHOT);
			if (effects != null) {
				effects.forEach(effect -> minimum.setValue(effect.effect().minimum().apply(level, random, minimum.floatValue())));
			}
		});
		return MathHelper.floor(minimum.floatValue());
	}

	public static int getMinimum(LivingEntity living) {
		int count = 0;
		for (ItemStack stack : living.getEquippedItems()) {
			count += getMinimum(living.getRandom(), stack);
		}
		return count;
	}

	public static int getMinimum(LivingEntity shooter, ItemStack weaponStack) {
		if (shooter instanceof PlayerEntity) {
			return getMinimum(shooter.getRandom(), weaponStack);
		}
		return getMinimum(shooter);
	}

	public static int getMaximum(Random random, ItemStack stack) {
		MutableFloat maximum = new MutableFloat(0);
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			List<EnchantmentEffectEntry<ScatterShotEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.SCATTER_SHOT);
			if (effects != null) {
				effects.forEach(effect -> maximum.setValue(effect.effect().maximum().apply(level, random, maximum.floatValue())));
			}
		});
		return MathHelper.floor(maximum.floatValue());
	}

	public static int getMaximum(LivingEntity living) {
		int count = 0;
		for (ItemStack stack : living.getEquippedItems()) {
			count += getMaximum(living.getRandom(), stack);
		}
		return count;
	}

	public static int getMaximum(LivingEntity shooter, ItemStack weaponStack) {
		if (shooter instanceof PlayerEntity) {
			return getMaximum(shooter.getRandom(), weaponStack);
		}
		return getMaximum(shooter);
	}

	public static Set<Item> getAllowedProjectiles(ItemStack stack) {
		Set<Item> allowedProjectiles = new HashSet<>();
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			List<EnchantmentEffectEntry<ScatterShotEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.SCATTER_SHOT);
			if (effects != null) {
				effects.forEach(effect -> allowedProjectiles.add(effect.effect().allowedProjectile()));
			}
		});
		return allowedProjectiles;
	}

	public static Set<Item> getAllowedProjectiles(LivingEntity living) {
		Set<Item> allowedProjectiles = new HashSet<>();
		for (ItemStack stack : living.getEquippedItems()) {
			allowedProjectiles.addAll(getAllowedProjectiles(stack));
		}
		return allowedProjectiles;
	}

	public static Set<Item> getAllowedProjectiles(LivingEntity shooter, ItemStack weaponStack) {
		if (shooter instanceof PlayerEntity) {
			return getAllowedProjectiles(weaponStack);
		}
		return getAllowedProjectiles(shooter);
	}
}
