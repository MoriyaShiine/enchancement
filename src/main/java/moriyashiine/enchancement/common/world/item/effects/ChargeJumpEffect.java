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

public record ChargeJumpEffect(EnchantmentValueEffect maximumCharge, EnchantmentValueEffect activeChargeRate, EnchantmentValueEffect jumpStrength) {
	public static final Codec<ChargeJumpEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("maximum_charge").forGetter(ChargeJumpEffect::maximumCharge),
					EnchantmentValueEffect.CODEC.fieldOf("active_charge_rate").forGetter(ChargeJumpEffect::activeChargeRate),
					EnchantmentValueEffect.CODEC.fieldOf("jump_strength").forGetter(ChargeJumpEffect::jumpStrength))
			.apply(instance, ChargeJumpEffect::new));

	public static int getMaximumCharge(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				ChargeJumpEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.CHARGE_JUMP);
				if (effect != null) {
					mutableFloat.setValue(effect.maximumCharge().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return Mth.floor(mutableFloat.floatValue());
	}

	public static float getActiveChargeRate(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat();
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				ChargeJumpEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.CHARGE_JUMP);
				if (effect != null) {
					mutableFloat.setValue(effect.activeChargeRate().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return mutableFloat.floatValue();
	}

	public static float getJumpStrength(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat();
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				ChargeJumpEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.CHARGE_JUMP);
				if (effect != null) {
					mutableFloat.setValue(effect.jumpStrength().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return mutableFloat.floatValue();
	}
}
