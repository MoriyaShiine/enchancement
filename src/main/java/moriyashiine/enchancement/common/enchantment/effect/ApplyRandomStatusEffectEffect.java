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
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public record ApplyRandomStatusEffectEffect(EnchantmentValueEffect duration, TagKey<StatusEffect> disallowedTag) {
	public static final Codec<ApplyRandomStatusEffectEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("duration").forGetter(ApplyRandomStatusEffectEffect::duration),
					TagKey.codec(RegistryKeys.STATUS_EFFECT).fieldOf("disallowed_tag").forGetter(ApplyRandomStatusEffectEffect::disallowedTag))
			.apply(instance, ApplyRandomStatusEffectEffect::new));

	public static void setValues(Random random, MutableFloat duration, AtomicReference<TagKey<StatusEffect>> disallowedTag, Iterable<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
				List<EnchantmentEffectEntry<ApplyRandomStatusEffectEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.APPLY_RANDOM_STATUS_EFFECT);
				if (effects != null) {
					effects.forEach(effect -> {
						duration.setValue(effect.effect().duration().apply(level, random, duration.floatValue()));
						disallowedTag.set(effect.effect().disallowedTag());
					});
				}
			});
		}
	}
}
