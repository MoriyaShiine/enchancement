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

public record DirectionMovementBurstEffect(EnchantmentValueEffect cooldown, EnchantmentValueEffect strength) {
	public static final Codec<DirectionMovementBurstEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("cooldown").forGetter(DirectionMovementBurstEffect::cooldown),
					EnchantmentValueEffect.CODEC.fieldOf("strength").forGetter(DirectionMovementBurstEffect::strength))
			.apply(instance, DirectionMovementBurstEffect::new));

	public static int getCooldown(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : entity.getArmorItems()) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
				DirectionMovementBurstEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.DIRECTION_MOVEMENT_BURST);
				if (effect != null) {
					mutableFloat.setValue(effect.cooldown().apply(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return MathHelper.floor(mutableFloat.floatValue() * 20);
	}

	public static float getStrength(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : entity.getArmorItems()) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
				DirectionMovementBurstEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.DIRECTION_MOVEMENT_BURST);
				if (effect != null) {
					mutableFloat.setValue(effect.strength().apply(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return mutableFloat.floatValue();
	}
}
