/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.enchantment.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableFloat;

public record LightningDashEffect(EnchantmentValueEffect chargeTime, EnchantmentValueEffect floatTime,
								  EnchantmentValueEffect dashStrength, EnchantmentValueEffect smashDamageMultiplier) {
	public static final Codec<LightningDashEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("charge_time").forGetter(LightningDashEffect::chargeTime),
					EnchantmentValueEffect.CODEC.fieldOf("float_time").forGetter(LightningDashEffect::floatTime),
					EnchantmentValueEffect.CODEC.fieldOf("dash_strength").forGetter(LightningDashEffect::dashStrength),
					EnchantmentValueEffect.CODEC.fieldOf("smash_damage_multiplier").forGetter(LightningDashEffect::smashDamageMultiplier))
			.apply(instance, LightningDashEffect::new));

	public static int getChargeTime(Random random, ItemStack stack) {
		MutableFloat mutableFloat = new MutableFloat(0);
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			LightningDashEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.LIGHTNING_DASH);
			if (effect != null) {
				mutableFloat.setValue(effect.chargeTime().apply(level, random, mutableFloat.floatValue()));
			}
		});
		return MathHelper.floor(mutableFloat.floatValue() * 20);
	}

	public static int getFloatTime(Random random, ItemStack stack) {
		MutableFloat mutableFloat = new MutableFloat(0);
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			LightningDashEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.LIGHTNING_DASH);
			if (effect != null) {
				mutableFloat.setValue(effect.floatTime().apply(level, random, mutableFloat.floatValue()));
			}
		});
		return MathHelper.floor(mutableFloat.floatValue() * 20);
	}

	public static float getDashStrength(Random random, ItemStack stack) {
		MutableFloat mutableFloat = new MutableFloat(0);
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			LightningDashEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.LIGHTNING_DASH);
			if (effect != null) {
				mutableFloat.setValue(effect.dashStrength().apply(level, random, mutableFloat.floatValue()));
			}
		});
		return mutableFloat.floatValue();
	}

	public static float getSmashDamageMultiplier(Random random, ItemStack stack) {
		MutableFloat mutableFloat = new MutableFloat(0);
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			LightningDashEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.LIGHTNING_DASH);
			if (effect != null) {
				mutableFloat.setValue(effect.smashDamageMultiplier().apply(level, random, mutableFloat.floatValue()));
			}
		});
		return mutableFloat.floatValue();
	}
}
