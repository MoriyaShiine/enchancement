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
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.List;

public record DelayedLaunchEffect(EnchantmentValueEffect maxDuration, EnchantmentValueEffect peakDuration,
								  EnchantmentValueEffect maxMultiplier, boolean allowRedirect) {
	public static final Codec<DelayedLaunchEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("max_duration").forGetter(DelayedLaunchEffect::maxDuration),
					EnchantmentValueEffect.CODEC.fieldOf("peak_duration").forGetter(DelayedLaunchEffect::peakDuration),
					EnchantmentValueEffect.CODEC.fieldOf("max_multiplier").forGetter(DelayedLaunchEffect::maxMultiplier),
					Codec.BOOL.fieldOf("allow_redirect").forGetter(DelayedLaunchEffect::allowRedirect))
			.apply(instance, DelayedLaunchEffect::new));

	public static void setValues(RandomSource random, MutableFloat maxDuration, MutableFloat peakDuration, MutableFloat maxMultiplier, MutableBoolean allowRedirect, Iterable<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				List<ConditionalEffect<DelayedLaunchEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.DELAYED_LAUNCH);
				if (effects != null) {
					effects.forEach(effect -> {
						maxDuration.setValue(effect.effect().maxDuration().process(level, random, maxDuration.floatValue()));
						peakDuration.setValue(effect.effect().peakDuration().process(level, random, peakDuration.floatValue()));
						maxMultiplier.setValue(effect.effect().maxMultiplier().process(level, random, maxMultiplier.floatValue()));
						allowRedirect.setValue(allowRedirect.booleanValue() || effect.effect().allowRedirect());
					});
				}
			});
		}
	}
}
