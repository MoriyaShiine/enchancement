/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public record ApplyRandomMobEffectEffect(EnchantmentValueEffect duration, TagKey<MobEffect> disallowedTag) {
	public static final Codec<ApplyRandomMobEffectEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("duration").forGetter(ApplyRandomMobEffectEffect::duration),
					TagKey.hashedCodec(Registries.MOB_EFFECT).fieldOf("disallowed_tag").forGetter(ApplyRandomMobEffectEffect::disallowedTag))
			.apply(instance, ApplyRandomMobEffectEffect::new));

	public static void setValues(RandomSource random, MutableFloat duration, AtomicReference<TagKey<MobEffect>> disallowedTag, Iterable<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				List<ConditionalEffect<ApplyRandomMobEffectEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.APPLY_RANDOM_MOB_EFFECT);
				if (effects != null) {
					effects.forEach(effect -> {
						duration.setValue(effect.effect().duration().process(level, random, duration.floatValue()));
						disallowedTag.set(effect.effect().disallowedTag());
					});
				}
			});
		}
	}
}
