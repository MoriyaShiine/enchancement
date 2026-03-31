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

public record AirJumpEffect(EnchantmentValueEffect airJumps, EnchantmentValueEffect airJumpStrength,
							EnchantmentValueEffect chargeCooldown, EnchantmentValueEffect jumpCooldown) {
	public static final Codec<AirJumpEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("air_jumps").forGetter(AirJumpEffect::airJumps),
					EnchantmentValueEffect.CODEC.fieldOf("air_jump_strength").forGetter(AirJumpEffect::airJumpStrength),
					EnchantmentValueEffect.CODEC.fieldOf("charge_cooldown").forGetter(AirJumpEffect::chargeCooldown),
					EnchantmentValueEffect.CODEC.fieldOf("jump_cooldown").forGetter(AirJumpEffect::jumpCooldown))
			.apply(instance, AirJumpEffect::new));

	public static int getAirJumps(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				AirJumpEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.AIR_JUMP);
				if (effect != null) {
					mutableFloat.setValue(effect.airJumps().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return Mth.floor(mutableFloat.floatValue());
	}

	public static float getAirJumpStrength(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				AirJumpEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.AIR_JUMP);
				if (effect != null) {
					mutableFloat.setValue(effect.airJumpStrength().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return mutableFloat.floatValue();
	}

	public static int getChargeCooldown(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				AirJumpEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.AIR_JUMP);
				if (effect != null) {
					mutableFloat.setValue(effect.chargeCooldown().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return Mth.floor(mutableFloat.floatValue() * 20);
	}

	public static int getJumpCooldown(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				AirJumpEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.AIR_JUMP);
				if (effect != null) {
					mutableFloat.setValue(effect.jumpCooldown().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return Mth.floor(mutableFloat.floatValue() * 20);
	}
}
