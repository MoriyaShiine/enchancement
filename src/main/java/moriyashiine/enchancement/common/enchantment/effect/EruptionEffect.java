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

public record EruptionEffect(EnchantmentValueEffect chargeTime, EnchantmentValueEffect jumpStrength,
							 EnchantmentValueEffect fireDuration) {
	public static final Codec<EruptionEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("charge_time").forGetter(EruptionEffect::chargeTime),
					EnchantmentValueEffect.CODEC.fieldOf("jump_strength").forGetter(EruptionEffect::jumpStrength),
					EnchantmentValueEffect.CODEC.fieldOf("fire_duration").forGetter(EruptionEffect::fireDuration))
			.apply(instance, EruptionEffect::new));

	public static int getChargeTime(Random random, ItemStack stack) {
		MutableFloat mutableFloat = new MutableFloat(0);
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			EruptionEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ERUPTION);
			if (effect != null) {
				mutableFloat.setValue(effect.chargeTime().apply(level, random, mutableFloat.floatValue()));
			}
		});
		return MathHelper.floor(mutableFloat.floatValue() * 20);
	}

	public static float getJumpStrength(Random random, ItemStack stack) {
		MutableFloat mutableFloat = new MutableFloat(0);
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			EruptionEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ERUPTION);
			if (effect != null) {
				mutableFloat.setValue(effect.jumpStrength().apply(level, random, mutableFloat.floatValue()));
			}
		});
		return mutableFloat.floatValue();
	}

	public static int getFireDuration(Random random, ItemStack stack) {
		MutableFloat mutableFloat = new MutableFloat(0);
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			EruptionEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ERUPTION);
			if (effect != null) {
				mutableFloat.setValue(effect.fireDuration().apply(level, random, mutableFloat.floatValue()));
			}
		});
		return MathHelper.floor(mutableFloat.floatValue() * 20);
	}
}
