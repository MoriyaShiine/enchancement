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

public record ChargeJumpEffect(EnchantmentValueEffect chargeTime, EnchantmentValueEffect strength) {
	public static final Codec<ChargeJumpEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("charge_time").forGetter(ChargeJumpEffect::chargeTime),
					EnchantmentValueEffect.CODEC.fieldOf("strength").forGetter(ChargeJumpEffect::strength))
			.apply(instance, ChargeJumpEffect::new));

	public static int getChargeTime(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat(0);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				ChargeJumpEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.CHARGE_JUMP);
				if (effect != null) {
					mutableFloat.setValue(effect.chargeTime().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return Mth.floor(mutableFloat.floatValue() * 20);
	}

	public static float getStrength(LivingEntity entity, float base) {
		MutableFloat mutableFloat = new MutableFloat(base);
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				ChargeJumpEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.CHARGE_JUMP);
				if (effect != null) {
					mutableFloat.setValue(effect.strength().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return mutableFloat.floatValue();
	}
}
