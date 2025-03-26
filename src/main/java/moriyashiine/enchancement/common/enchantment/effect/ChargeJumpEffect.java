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

public record ChargeJumpEffect(EnchantmentValueEffect chargeTime, EnchantmentValueEffect strength) {
	public static final Codec<ChargeJumpEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("charge_time").forGetter(ChargeJumpEffect::chargeTime),
					EnchantmentValueEffect.CODEC.fieldOf("strength").forGetter(ChargeJumpEffect::strength))
			.apply(instance, ChargeJumpEffect::new));

	public static int getChargeTime(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
				ChargeJumpEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.CHARGE_JUMP);
				if (effect != null) {
					mutableFloat.setValue(effect.chargeTime().apply(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return MathHelper.floor(mutableFloat.floatValue() * 20);
	}

	public static float getStrength(LivingEntity entity, float base) {
		MutableFloat mutableFloat = new MutableFloat(base);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
				ChargeJumpEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.CHARGE_JUMP);
				if (effect != null) {
					mutableFloat.setValue(effect.strength().apply(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return mutableFloat.floatValue();
	}
}
