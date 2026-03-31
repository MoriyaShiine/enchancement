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

public record BrimstoneEffect(EnchantmentValueEffect chargeTimeMultiplier) {
	public static final Codec<BrimstoneEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("charge_time_multiplier").forGetter(BrimstoneEffect::chargeTimeMultiplier))
			.apply(instance, BrimstoneEffect::new));

	public static float getChargeTimeMultiplier(RandomSource random, ItemStack stack) {
		MutableFloat value = new MutableFloat();
		EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
			BrimstoneEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.BRIMSTONE);
			if (effect != null) {
				value.setValue(effect.chargeTimeMultiplier().process(level, random, value.floatValue()));
			}
		});
		return value.floatValue();
	}

	public static int getBrimstoneDamage(float progress) {
		return (int) (6 * progress) * 2;
	}
}
