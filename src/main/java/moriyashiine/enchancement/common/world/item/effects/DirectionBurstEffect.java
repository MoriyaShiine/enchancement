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

public record DirectionBurstEffect(EnchantmentValueEffect cooldown, EnchantmentValueEffect groundStrength,
								   EnchantmentValueEffect airStrength) {
	public static final Codec<DirectionBurstEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("cooldown").forGetter(DirectionBurstEffect::cooldown),
					EnchantmentValueEffect.CODEC.fieldOf("ground_strength").forGetter(DirectionBurstEffect::groundStrength),
					EnchantmentValueEffect.CODEC.fieldOf("air_strength").forGetter(DirectionBurstEffect::airStrength))
			.apply(instance, DirectionBurstEffect::new));

	public static int getCooldown(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				DirectionBurstEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.DIRECTION_BURST);
				if (effect != null) {
					mutableFloat.setValue(effect.cooldown().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return Mth.floor(mutableFloat.floatValue() * 20);
	}

	public static float getGroundStrength(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				DirectionBurstEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.DIRECTION_BURST);
				if (effect != null) {
					mutableFloat.setValue(effect.groundStrength().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return mutableFloat.floatValue();
	}

	public static float getAirStrength(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				DirectionBurstEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.DIRECTION_BURST);
				if (effect != null) {
					mutableFloat.setValue(effect.airStrength().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return mutableFloat.floatValue();
	}
}
