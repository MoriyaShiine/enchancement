/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.enchantment.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.mutable.MutableFloat;

public record GlideEffect(EnchantmentValueEffect minDuration, EnchantmentValueEffect maxDuration) {
	public static final Codec<GlideEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("min_duration").forGetter(GlideEffect::minDuration),
					EnchantmentValueEffect.CODEC.fieldOf("max_duration").forGetter(GlideEffect::maxDuration))
			.apply(instance, GlideEffect::new));

	public static int getMinDuration(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : entity.getAllArmorItems()) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
				GlideEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.GLIDE);
				if (effect != null) {
					mutableFloat.setValue(effect.minDuration().apply(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return MathHelper.floor(mutableFloat.floatValue() * 20);
	}

	public static int getMaxDuration(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : entity.getAllArmorItems()) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
				GlideEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.GLIDE);
				if (effect != null) {
					mutableFloat.setValue(effect.maxDuration().apply(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return MathHelper.floor(mutableFloat.floatValue() * 20);
	}
}
