/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.SubmersionGate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import org.apache.commons.lang3.mutable.MutableFloat;

public record ModifySubmergedMovementSpeedEffect(EnchantmentValueEffect modifier, SubmersionGate gate) {
	public static final Codec<ModifySubmergedMovementSpeedEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("modifier").forGetter(ModifySubmergedMovementSpeedEffect::modifier),
					Codec.STRING.fieldOf("gate").forGetter(e -> e.gate().name()))
			.apply(instance, (enchantmentValueEffect, gate) -> new ModifySubmergedMovementSpeedEffect(enchantmentValueEffect, SubmersionGate.valueOf(gate))));

	public static float getValue(LivingEntity entity) {
		if (entity.isSpectator()) {
			return 0;
		}
		MutableFloat value = new MutableFloat();
		for (ItemStack stack : EnchancementUtil.getArmorItems(entity)) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				ModifySubmergedMovementSpeedEffect effect = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.MODIFY_SUBMERGED_MOVEMENT_SPEED);
				if (effect != null && shouldApply(entity, effect)) {
					value.setValue(effect.modifier().process(level, entity.getRandom(), value.floatValue()));
				}
			});
		}
		return value.floatValue();
	}

	private static boolean shouldApply(LivingEntity entity, ModifySubmergedMovementSpeedEffect effect) {
		return entity.isInWaterOrRain() || ModEntityComponents.EXTENDED_WATER_TIME.get(entity).getTicksWet() > 0 || SLibUtils.isSubmerged(entity, effect.gate());
	}
}
