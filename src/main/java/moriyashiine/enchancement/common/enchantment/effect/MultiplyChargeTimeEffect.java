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

public record MultiplyChargeTimeEffect(EnchantmentValueEffect multiplier) {
	public static final Codec<MultiplyChargeTimeEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("multiplier").forGetter(MultiplyChargeTimeEffect::multiplier))
			.apply(instance, MultiplyChargeTimeEffect::new));

	public static float getMultiplier(Random random, ItemStack stack) {
		MutableFloat mutableFloat = new MutableFloat(1);
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			MultiplyChargeTimeEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.MULTIPLY_CHARGE_TIME);
			if (effect != null) {
				mutableFloat.setValue(effect.multiplier().apply(level, random, mutableFloat.floatValue()));
			}
		});
		return mutableFloat.floatValue();
	}
}
