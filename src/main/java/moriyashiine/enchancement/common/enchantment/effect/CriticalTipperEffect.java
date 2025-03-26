/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.enchantment.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.List;

public record CriticalTipperEffect(EnchantmentValueEffect distanceLeniency, ParticleType<?> particleType) {
	public static final Codec<CriticalTipperEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("distance_leniency").forGetter(CriticalTipperEffect::distanceLeniency),
					Registries.PARTICLE_TYPE.getCodec().fieldOf("particle_type").forGetter(CriticalTipperEffect::particleType))
			.apply(instance, CriticalTipperEffect::new));

	public static float getDistanceLeniency(ItemStack stack, Random random) {
		MutableFloat mutableFloat = new MutableFloat(0);
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			List<EnchantmentEffectEntry<CriticalTipperEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.CRITICAL_TIPPER);
			if (effects != null) {
				effects.forEach(effect -> mutableFloat.setValue(effect.effect().distanceLeniency().apply(level, random, mutableFloat.floatValue())));
			}
		});
		return mutableFloat.floatValue();
	}

	public static ParticleType<?> getParticleType(ItemStack stack) {
		final ParticleType<?>[] particleType = {null};
		EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
			List<EnchantmentEffectEntry<CriticalTipperEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.CRITICAL_TIPPER);
			if (effects != null) {
				effects.forEach(effect -> particleType[0] = effect.effect().particleType());
			}
		});
		return particleType[0];
	}
}
