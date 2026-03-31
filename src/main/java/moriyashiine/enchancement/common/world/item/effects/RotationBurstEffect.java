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

public record RotationBurstEffect(EnchantmentValueEffect cooldown, EnchantmentValueEffect strength,
								  EnchantmentValueEffect wavedashTicks, EnchantmentValueEffect wavedashStrength) {
	public static final Codec<RotationBurstEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("cooldown").forGetter(RotationBurstEffect::cooldown),
					EnchantmentValueEffect.CODEC.fieldOf("strength").forGetter(RotationBurstEffect::strength),
					EnchantmentValueEffect.CODEC.fieldOf("wavedash_ticks").forGetter(RotationBurstEffect::wavedashTicks),
					EnchantmentValueEffect.CODEC.fieldOf("wavedash_strength").forGetter(RotationBurstEffect::wavedashStrength))
			.apply(instance, RotationBurstEffect::new));

	public static int getCooldown(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				RotationBurstEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ROTATION_BURST);
				if (effect != null) {
					mutableFloat.setValue(effect.cooldown().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return Mth.floor(mutableFloat.floatValue() * 20);
	}

	public static float getStrength(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				RotationBurstEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ROTATION_BURST);
				if (effect != null) {
					mutableFloat.setValue(effect.strength().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return mutableFloat.floatValue();
	}

	public static int getWavedashTicks(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				RotationBurstEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ROTATION_BURST);
				if (effect != null) {
					mutableFloat.setValue(effect.wavedashTicks().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return Mth.floor(mutableFloat.floatValue());
	}

	public static float getWavedashStrength(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				RotationBurstEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.ROTATION_BURST);
				if (effect != null) {
					mutableFloat.setValue(effect.wavedashStrength().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return mutableFloat.floatValue();
	}
}
