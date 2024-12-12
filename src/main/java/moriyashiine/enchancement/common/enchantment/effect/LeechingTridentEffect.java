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
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.List;

public record LeechingTridentEffect(EnchantmentValueEffect damage, EnchantmentValueEffect healAmount,
									EnchantmentValueEffect duration) {
	public static final Codec<LeechingTridentEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("damage").forGetter(LeechingTridentEffect::damage),
					EnchantmentValueEffect.CODEC.fieldOf("heal_amount").forGetter(LeechingTridentEffect::healAmount),
					EnchantmentValueEffect.CODEC.fieldOf("duration").forGetter(LeechingTridentEffect::duration))
			.apply(instance, LeechingTridentEffect::new));

	public static void setValues(Random random, MutableFloat damage, MutableFloat healAmount, MutableFloat duration, Iterable<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
				List<EnchantmentEffectEntry<LeechingTridentEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.LEECHING_TRIDENT);
				if (effects != null) {
					effects.forEach(effect -> {
						damage.setValue(effect.effect().damage().apply(level, random, damage.floatValue()));
						healAmount.setValue(effect.effect().healAmount().apply(level, random, healAmount.floatValue()));
						duration.setValue(effect.effect().duration().apply(level, random, duration.floatValue()));
					});
				}
			});
		}
	}
}
