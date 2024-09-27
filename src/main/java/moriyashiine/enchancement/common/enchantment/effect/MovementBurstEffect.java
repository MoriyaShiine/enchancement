/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.enchantment.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.mutable.MutableFloat;

public record MovementBurstEffect(EnchantmentValueEffect cooldown, EnchantmentValueEffect strength) {
	public static final Codec<MovementBurstEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("charge_cooldown").forGetter(MovementBurstEffect::cooldown),
					EnchantmentValueEffect.CODEC.fieldOf("strength").forGetter(MovementBurstEffect::strength))
			.apply(instance, MovementBurstEffect::new));

	public static int getCooldown(ComponentType<MovementBurstEffect> component, LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : entity.getArmorItems()) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
				MovementBurstEffect effect = enchantment.value().effects().get(component);
				if (effect != null) {
					mutableFloat.setValue(effect.cooldown().apply(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return MathHelper.floor(mutableFloat.floatValue() * 20);
	}

	public static float getStrength(ComponentType<MovementBurstEffect> component, LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : entity.getArmorItems()) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
				MovementBurstEffect effect = enchantment.value().effects().get(component);
				if (effect != null) {
					mutableFloat.setValue(effect.strength().apply(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return mutableFloat.floatValue();
	}
}
