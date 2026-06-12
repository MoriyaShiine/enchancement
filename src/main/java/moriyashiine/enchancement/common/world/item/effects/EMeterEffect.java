/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.EnchancementEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import org.apache.commons.lang3.mutable.MutableFloat;

public record EMeterEffect(EnchantmentValueEffect speedMultiplier, EnchantmentValueEffect floatStrength) {
	public static final Codec<EMeterEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			EnchantmentValueEffect.CODEC.fieldOf("speed_multiplier").forGetter(EMeterEffect::speedMultiplier),
			EnchantmentValueEffect.CODEC.fieldOf("float_strength").forGetter(EMeterEffect::floatStrength)
	).apply(instance, EMeterEffect::new));

	public static float getSpeedMultiplier(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat();
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				EMeterEffect effect = enchantment.value().effects().get(EnchancementEnchantmentEffectComponentTypes.E_METER);
				if (effect != null) {
					mutableFloat.setValue(effect.speedMultiplier().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return mutableFloat.floatValue();
	}

	public static float getFloatStrength(LivingEntity entity) {
		MutableFloat mutableFloat = new MutableFloat();
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				EMeterEffect effect = enchantment.value().effects().get(EnchancementEnchantmentEffectComponentTypes.E_METER);
				if (effect != null) {
					mutableFloat.setValue(effect.floatStrength().process(level, entity.getRandom(), mutableFloat.floatValue()));
				}
			});
		}
		return mutableFloat.floatValue();
	}
}
