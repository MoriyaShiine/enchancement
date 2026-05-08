/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.List;

public record PhaseEffect(EnchantmentValueEffect maxPhaseBlocks, boolean bypassShields) {
	public static final Codec<PhaseEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					EnchantmentValueEffect.CODEC.fieldOf("max_phase_blocks").forGetter(PhaseEffect::maxPhaseBlocks),
					Codec.BOOL.fieldOf("bypass_shields").forGetter(PhaseEffect::bypassShields))
			.apply(instance, PhaseEffect::new));

	public static void setValues(RandomSource random, MutableFloat maxPhaseBlocks, MutableBoolean bypassShields, Iterable<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				List<ConditionalEffect<PhaseEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.PHASE);
				if (effects != null) {
					effects.forEach(effect -> {
						maxPhaseBlocks.setValue(effect.effect().maxPhaseBlocks().process(level, random, maxPhaseBlocks.floatValue()));
						bypassShields.setValue(bypassShields.booleanValue() | effect.effect().bypassShields());
					});
				}
			});
		}
	}
}
