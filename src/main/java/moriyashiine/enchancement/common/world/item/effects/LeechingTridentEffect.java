/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.List;

public record LeechingTridentEffect(EnchantmentValueEffect damage, EnchantmentValueEffect healAmount, EnchantmentValueEffect duration) {
	public static final Codec<LeechingTridentEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("damage").forGetter(LeechingTridentEffect::damage),
					EnchantmentValueEffect.CODEC.fieldOf("heal_amount").forGetter(LeechingTridentEffect::healAmount),
					EnchantmentValueEffect.CODEC.fieldOf("duration").forGetter(LeechingTridentEffect::duration))
			.apply(instance, LeechingTridentEffect::new));

	public static void setValues(RandomSource random, MutableFloat damage, MutableFloat healAmount, MutableFloat duration, Iterable<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				List<ConditionalEffect<LeechingTridentEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.LEECHING_TRIDENT);
				if (effects != null) {
					effects.forEach(effect -> {
						damage.setValue(effect.effect().damage().process(level, random, damage.floatValue()));
						healAmount.setValue(effect.effect().healAmount().process(level, random, healAmount.floatValue()));
						duration.setValue(effect.effect().duration().process(level, random, duration.floatValue()));
					});
				}
			});
		}
	}
}
