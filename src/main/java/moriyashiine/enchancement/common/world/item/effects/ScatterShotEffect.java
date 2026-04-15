/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record ScatterShotEffect(EnchantmentValueEffect minimum, EnchantmentValueEffect maximum, Item allowedProjectile) {
	public static final Codec<ScatterShotEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("minimum").forGetter(ScatterShotEffect::minimum),
					EnchantmentValueEffect.CODEC.fieldOf("maximum").forGetter(ScatterShotEffect::maximum),
					BuiltInRegistries.ITEM.byNameCodec().fieldOf("allowed_projectile").forGetter(ScatterShotEffect::allowedProjectile))
			.apply(instance, ScatterShotEffect::new));

	public static boolean hasScatterShot = false;

	public static int getMinimum(RandomSource random, ItemStack stack) {
		MutableFloat minimum = new MutableFloat(0);
		EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
			List<ConditionalEffect<ScatterShotEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.SCATTER_SHOT);
			if (effects != null) {
				effects.forEach(effect -> minimum.setValue(effect.effect().minimum().process(level, random, minimum.floatValue())));
			}
		});
		return Mth.floor(minimum.floatValue());
	}

	public static int getMinimum(LivingEntity entity) {
		int count = 0;
		for (ItemStack stack : EnchancementUtil.getHeldItems(entity)) {
			count += getMinimum(entity.getRandom(), stack);
		}
		return count;
	}

	public static int getMinimum(LivingEntity entity, ItemStack weaponStack) {
		if (entity instanceof Player) {
			return getMinimum(entity.getRandom(), weaponStack);
		}
		return getMinimum(entity);
	}

	public static int getMaximum(RandomSource random, ItemStack stack) {
		MutableFloat maximum = new MutableFloat(0);
		EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
			List<ConditionalEffect<ScatterShotEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.SCATTER_SHOT);
			if (effects != null) {
				effects.forEach(effect -> maximum.setValue(effect.effect().maximum().process(level, random, maximum.floatValue())));
			}
		});
		return Mth.floor(maximum.floatValue());
	}

	public static int getMaximum(LivingEntity entity) {
		int count = 0;
		for (ItemStack stack : EnchancementUtil.getHeldItems(entity)) {
			count += getMaximum(entity.getRandom(), stack);
		}
		return count;
	}

	public static int getMaximum(LivingEntity shooter, ItemStack weaponStack) {
		if (shooter instanceof Player) {
			return getMaximum(shooter.getRandom(), weaponStack);
		}
		return getMaximum(shooter);
	}

	public static Set<Item> getAllowedProjectiles(ItemStack stack) {
		Set<Item> allowedProjectiles = new HashSet<>();
		EnchantmentHelper.runIterationOnItem(stack, (enchantment, _) -> {
			List<ConditionalEffect<ScatterShotEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.SCATTER_SHOT);
			if (effects != null) {
				effects.forEach(effect -> allowedProjectiles.add(effect.effect().allowedProjectile()));
			}
		});
		return allowedProjectiles;
	}

	public static Set<Item> getAllowedProjectiles(LivingEntity entity) {
		Set<Item> allowedProjectiles = new HashSet<>();
		for (ItemStack stack : EnchancementUtil.getHeldItems(entity)) {
			allowedProjectiles.addAll(getAllowedProjectiles(stack));
		}
		return allowedProjectiles;
	}

	public static Set<Item> getAllowedProjectiles(LivingEntity entity, ItemStack weaponStack) {
		if (entity instanceof Player) {
			return getAllowedProjectiles(weaponStack);
		}
		return getAllowedProjectiles(entity);
	}
}
