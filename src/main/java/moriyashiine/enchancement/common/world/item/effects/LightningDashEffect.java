/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import org.apache.commons.lang3.mutable.MutableFloat;

public record LightningDashEffect(EnchantmentValueEffect floatTime, EnchantmentValueEffect lungeStrength,
								  EnchantmentValueEffect smashStrength, EnchantmentValueEffect smashDamageMultiplier) {
	public static final Codec<LightningDashEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("float_time").forGetter(LightningDashEffect::floatTime),
					EnchantmentValueEffect.CODEC.fieldOf("lunge_strength").forGetter(LightningDashEffect::lungeStrength),
					EnchantmentValueEffect.CODEC.fieldOf("smash_strength").forGetter(LightningDashEffect::smashStrength),
					EnchantmentValueEffect.CODEC.fieldOf("smash_damage_multiplier").forGetter(LightningDashEffect::smashDamageMultiplier))
			.apply(instance, LightningDashEffect::new));

	public static int getFloatTime(RandomSource random, ItemStack stack) {
		MutableFloat mutableFloat = new MutableFloat(0);
		EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
			LightningDashEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.LIGHTNING_DASH);
			if (effect != null) {
				mutableFloat.setValue(effect.floatTime().process(level, random, mutableFloat.floatValue()));
			}
		});
		return Mth.floor(mutableFloat.floatValue() * 20);
	}

	public static float getLungeStrength(RandomSource random, ItemStack stack) {
		MutableFloat mutableFloat = new MutableFloat(0);
		EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
			LightningDashEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.LIGHTNING_DASH);
			if (effect != null) {
				mutableFloat.setValue(effect.lungeStrength().process(level, random, mutableFloat.floatValue()));
			}
		});
		return mutableFloat.floatValue();
	}

	public static float getSmashStrength(RandomSource random, ItemStack stack) {
		MutableFloat mutableFloat = new MutableFloat(0);
		EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
			LightningDashEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.LIGHTNING_DASH);
			if (effect != null) {
				mutableFloat.setValue(effect.smashStrength().process(level, random, mutableFloat.floatValue()));
			}
		});
		return mutableFloat.floatValue();
	}

	public static float getSmashDamageMultiplier(RandomSource random, ItemStack stack) {
		MutableFloat mutableFloat = new MutableFloat(0);
		EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
			LightningDashEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.LIGHTNING_DASH);
			if (effect != null) {
				mutableFloat.setValue(effect.smashDamageMultiplier().process(level, random, mutableFloat.floatValue()));
			}
		});
		return mutableFloat.floatValue();
	}
}
