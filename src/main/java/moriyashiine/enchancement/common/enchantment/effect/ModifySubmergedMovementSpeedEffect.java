/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.enchantment.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.SubmersionGate;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableFloat;

public record ModifySubmergedMovementSpeedEffect(EnchantmentValueEffect modifier, SubmersionGate gate) {
	public static final Codec<ModifySubmergedMovementSpeedEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("modifier").forGetter(ModifySubmergedMovementSpeedEffect::modifier),
					Codec.STRING.fieldOf("gate").forGetter(e -> e.gate().name()))
			.apply(instance, (enchantmentValueEffect, gate) -> new ModifySubmergedMovementSpeedEffect(enchantmentValueEffect, SubmersionGate.valueOf(gate))));

	public static float getValue(LivingEntity entity) {
		MutableFloat value = new MutableFloat();
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
				ModifySubmergedMovementSpeedEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.MODIFY_SUBMERGED_MOVEMENT_SPEED);
				if (effect != null && shouldApply(entity, effect)) {
					value.setValue(effect.modifier().apply(level, entity.getRandom(), value.floatValue()));
				}
			});
		}
		return value.floatValue();
	}

	private static boolean shouldApply(LivingEntity entity, ModifySubmergedMovementSpeedEffect effect) {
		return entity.isTouchingWaterOrRain() || ModEntityComponents.EXTENDED_WATER_TIME.get(entity).getTicksWet() > 0 || SLibUtils.isSubmerged(entity, effect.gate());
	}
}
