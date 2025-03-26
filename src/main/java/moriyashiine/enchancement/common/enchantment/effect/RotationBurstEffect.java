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
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.mutable.MutableFloat;

public record RotationBurstEffect(EnchantmentValueEffect cooldown, EnchantmentValueEffect strength,
								  EnchantmentValueEffect wavedashTicks,
								  EnchantmentValueEffect wavedashStrength) {
	public static final Codec<RotationBurstEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("cooldown").forGetter(RotationBurstEffect::cooldown),
					EnchantmentValueEffect.CODEC.fieldOf("strength").forGetter(RotationBurstEffect::strength),
					EnchantmentValueEffect.CODEC.fieldOf("wavedash_ticks").forGetter(RotationBurstEffect::wavedashTicks),
					EnchantmentValueEffect.CODEC.fieldOf("wavedash_strength").forGetter(RotationBurstEffect::wavedashStrength))
			.apply(instance, RotationBurstEffect::new));

	public static int getCooldown(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
				RotationBurstEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ROTATION_BURST);
				if (effect != null) {
					mutableFloat.setValue(effect.cooldown().apply(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return MathHelper.floor(mutableFloat.floatValue() * 20);
	}

	public static float getStrength(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
				RotationBurstEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ROTATION_BURST);
				if (effect != null) {
					mutableFloat.setValue(effect.strength().apply(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return mutableFloat.floatValue();
	}

	public static int getWavedashTicks(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
				RotationBurstEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ROTATION_BURST);
				if (effect != null) {
					mutableFloat.setValue(effect.wavedashTicks().apply(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return MathHelper.floor(mutableFloat.floatValue());
	}

	public static float getWavedashStrength(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
				RotationBurstEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ROTATION_BURST);
				if (effect != null) {
					mutableFloat.setValue(effect.wavedashStrength().apply(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return mutableFloat.floatValue();
	}
}
