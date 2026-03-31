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

public record DisarmingFishingBobberEffect(boolean stealsFromPlayers, EnchantmentValueEffect playerCooldown,
										   EnchantmentValueEffect userCooldown) {
	public static final Codec<DisarmingFishingBobberEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					Codec.BOOL.fieldOf("steals_from_players").forGetter(DisarmingFishingBobberEffect::stealsFromPlayers),
					EnchantmentValueEffect.CODEC.fieldOf("player_cooldown").forGetter(DisarmingFishingBobberEffect::playerCooldown),
					EnchantmentValueEffect.CODEC.fieldOf("user_cooldown").forGetter(DisarmingFishingBobberEffect::userCooldown))
			.apply(instance, DisarmingFishingBobberEffect::new));

	public static void setValues(RandomSource random, MutableBoolean enabled, MutableBoolean stealsFromPlayers, MutableFloat playerCooldown, MutableFloat userCooldown, Iterable<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			EnchantmentHelper.runIterationOnItem(stack, (enchantment, level) -> {
				List<ConditionalEffect<DisarmingFishingBobberEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.DISARMING_FISHING_BOBBER);
				if (effects != null) {
					effects.forEach(effect -> {
						enabled.setValue(true);
						stealsFromPlayers.setValue(stealsFromPlayers.booleanValue() || effect.effect().stealsFromPlayers());
						playerCooldown.setValue(effect.effect().playerCooldown().process(level, random, playerCooldown.floatValue()));
						userCooldown.setValue(effect.effect().userCooldown().process(level, random, userCooldown.floatValue()));
					});
				}
			});
		}
	}
}
