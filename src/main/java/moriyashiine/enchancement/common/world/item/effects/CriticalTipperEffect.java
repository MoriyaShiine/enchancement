/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.List;

public record CriticalTipperEffect(EnchantmentValueEffect distanceLeniency, ParticleType<?> particleType) {
	public static final Codec<CriticalTipperEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("distance_leniency").forGetter(CriticalTipperEffect::distanceLeniency),
					BuiltInRegistries.PARTICLE_TYPE.byNameCodec().fieldOf("particle_type").forGetter(CriticalTipperEffect::particleType))
			.apply(instance, CriticalTipperEffect::new));

	public static float getDistanceLeniency(ItemStack stack, RandomSource random) {
		MutableFloat mutableFloat = new MutableFloat(0);
		EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
			List<ConditionalEffect<CriticalTipperEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.CRITICAL_TIPPER);
			if (effects != null) {
				effects.forEach(effect -> mutableFloat.setValue(effect.effect().distanceLeniency().process(level, random, mutableFloat.floatValue())));
			}
		});
		return mutableFloat.floatValue();
	}

	public static ParticleType<?> getParticleType(ItemStack stack) {
		final ParticleType<?>[] particleType = {null};
		EnchantmentHelper.runIterationOnItem(stack, (enchantment, _) -> {
			List<ConditionalEffect<CriticalTipperEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.CRITICAL_TIPPER);
			if (effects != null) {
				effects.forEach(effect -> particleType[0] = effect.effect().particleType());
			}
		});
		return particleType[0];
	}
}
