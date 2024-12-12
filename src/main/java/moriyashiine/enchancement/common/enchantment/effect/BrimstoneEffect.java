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
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableFloat;

public record BrimstoneEffect(EnchantmentValueEffect chargeTimeMultiplier) {
	public static final Codec<BrimstoneEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("charge_time_multiplier").forGetter(BrimstoneEffect::chargeTimeMultiplier))
			.apply(instance, BrimstoneEffect::new));

	public static float getChargeTimeMultiplier(Random random, ItemStack stack) {
		MutableFloat value = new MutableFloat();
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			BrimstoneEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.BRIMSTONE);
			if (effect != null) {
				value.setValue(effect.chargeTimeMultiplier().apply(level, random, value.floatValue()));
			}
		});
		return value.floatValue();
	}

	public static int getBrimstoneDamage(float progress) {
		return (int) (6 * progress) * 2;
	}
}
