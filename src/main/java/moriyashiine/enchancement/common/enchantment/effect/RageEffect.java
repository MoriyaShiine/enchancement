/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.enchantment.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.mutable.MutableFloat;

public record RageEffect(EnchantmentValueEffect damageDealtModifier, EnchantmentValueEffect damageTakenModifier,
						 EnchantmentValueEffect movementSpeedModifier) {
	public static final Codec<RageEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("damage_dealt_modifier").forGetter(RageEffect::damageDealtModifier),
					EnchantmentValueEffect.CODEC.fieldOf("damage_taken_modifier").forGetter(RageEffect::damageTakenModifier),
					EnchantmentValueEffect.CODEC.fieldOf("movement_speed_modifier").forGetter(RageEffect::movementSpeedModifier))
			.apply(instance, RageEffect::new));

	// damage dealt
	public static int color = -1;

	public static float getDamageDealtModifier(LivingEntity entity, ItemStack stack) {
		MutableFloat value = new MutableFloat();
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			RageEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.RAGE);
			if (effect != null) {
				value.setValue(effect.damageDealtModifier().apply(level, entity.getRandom(), value.floatValue()));
			}
		});
		return (10 - Math.max(3, EnchancementUtil.getFlooredHealth(entity))) * value.floatValue();
	}

	public static float getDamageDealtModifierMax(LivingEntity entity, ItemStack stack) {
		MutableFloat value = new MutableFloat();
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			RageEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.RAGE);
			if (effect != null) {
				value.setValue(effect.damageDealtModifier().apply(level, entity.getRandom(), value.floatValue()));
			}
		});
		return 7 * value.floatValue();
	}

	public static int getColor(Entity entity, ItemStack stack) {
		if (entity instanceof LivingEntity living && !living.isSpectator()) {
			float damageBonus = getDamageDealtModifier(living, stack);
			if (damageBonus > 0) {
				float other = 1 - damageBonus / getDamageDealtModifierMax(living, stack);
				return (0xFF << 24) | (0xFF << 16) | (((int) (other * 255 + 0.5) & 0xFF) << 8) | ((int) (other * 255 + 0.5) & 0xFF);
			}
		}
		return -1;
	}

	// damage taken
	public static float getDamageTakenModifier(LivingEntity entity, ItemStack stack) {
		if (entity == null || entity.isSpectator()) {
			return 1;
		}
		MutableFloat value = new MutableFloat();
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			RageEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.RAGE);
			if (effect != null) {
				value.setValue(effect.damageTakenModifier().apply(level, entity.getRandom(), value.floatValue()));
			}
		});
		return Math.max(0, 1 - MathHelper.ceil((10 - Math.max(3, EnchancementUtil.getFlooredHealth(entity))) * value.floatValue() * 100) / 100F);
	}

	public static float getDamageTakenModifier(LivingEntity entity) {
		float value = 1;
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			value *= getDamageTakenModifier(entity, stack);
		}
		return Math.max(0, value);
	}

	// movement speed
	public static float getMovementSpeedModifier(LivingEntity entity, ItemStack stack) {
		if (entity == null || entity.isSpectator()) {
			return 0;
		}
		MutableFloat value = new MutableFloat();
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			RageEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.RAGE);
			if (effect != null) {
				value.setValue(effect.movementSpeedModifier().apply(level, entity.getRandom(), value.floatValue()));
			}
		});
		return (10 - Math.max(3, EnchancementUtil.getFlooredHealth(entity))) * value.floatValue();
	}

	public static float getMovementSpeedModifier(LivingEntity entity) {
		float value = 1;
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			value += getMovementSpeedModifier(entity, stack);
		}
		return value;
	}
}
