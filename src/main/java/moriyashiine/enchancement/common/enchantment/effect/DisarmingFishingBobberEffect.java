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
import net.minecraft.util.math.random.Random;
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

	public static void setValues(Random random, MutableBoolean enabled, MutableBoolean stealsFromPlayers, MutableFloat playerCooldown, MutableFloat userCooldown, Iterable<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
				List<EnchantmentEffectEntry<DisarmingFishingBobberEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.DISARMING_FISHING_BOBBER);
				if (effects != null) {
					effects.forEach(effect -> {
						enabled.setValue(true);
						stealsFromPlayers.setValue(stealsFromPlayers.booleanValue() || effect.effect().stealsFromPlayers());
						playerCooldown.setValue(effect.effect().playerCooldown().apply(level, random, playerCooldown.floatValue()));
						userCooldown.setValue(effect.effect().userCooldown().apply(level, random, userCooldown.floatValue()));
					});
				}
			});
		}
	}
}
