/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import org.apache.commons.lang3.mutable.MutableFloat;

public record EruptionEffect(EnchantmentValueEffect jumpStrength, EnchantmentValueEffect fireDuration) {
	public static final Codec<EruptionEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("jump_strength").forGetter(EruptionEffect::jumpStrength),
					EnchantmentValueEffect.CODEC.fieldOf("fire_duration").forGetter(EruptionEffect::fireDuration))
			.apply(instance, EruptionEffect::new));

	public static float getJumpStrength(RandomSource random, ItemStack stack) {
		MutableFloat mutableFloat = new MutableFloat(0);
		EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
			EruptionEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ERUPTION);
			if (effect != null) {
				mutableFloat.setValue(effect.jumpStrength().process(level, random, mutableFloat.floatValue()));
			}
		});
		return mutableFloat.floatValue();
	}

	public static float getFireDuration(RandomSource random, ItemStack stack) {
		MutableFloat mutableFloat = new MutableFloat(0);
		EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
			EruptionEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ERUPTION);
			if (effect != null) {
				mutableFloat.setValue(effect.fireDuration().process(level, random, mutableFloat.floatValue()));
			}
		});
		return mutableFloat.floatValue();
	}
}
