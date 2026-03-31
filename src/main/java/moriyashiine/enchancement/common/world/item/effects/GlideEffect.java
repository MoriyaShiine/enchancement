/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import org.apache.commons.lang3.mutable.MutableFloat;

public record GlideEffect(EnchantmentValueEffect minDuration, EnchantmentValueEffect maxDuration) {
	public static final Codec<GlideEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("min_duration").forGetter(GlideEffect::minDuration),
					EnchantmentValueEffect.CODEC.fieldOf("max_duration").forGetter(GlideEffect::maxDuration))
			.apply(instance, GlideEffect::new));

	public static int getMinDuration(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				GlideEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.GLIDE);
				if (effect != null) {
					mutableFloat.setValue(effect.minDuration().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return Mth.floor(mutableFloat.floatValue() * 20);
	}

	public static int getMaxDuration(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				GlideEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.GLIDE);
				if (effect != null) {
					mutableFloat.setValue(effect.maxDuration().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return Mth.floor(mutableFloat.floatValue() * 20);
	}
}
