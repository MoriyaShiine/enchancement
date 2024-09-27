/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.enchantment.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.List;

public record TeleportOnHitEffect(boolean teleportsOnBlockHit, boolean teleportsOnEntityHit) {
	public static final Codec<TeleportOnHitEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					Codec.BOOL.fieldOf("teleports_on_block_hit").forGetter(TeleportOnHitEffect::teleportsOnBlockHit),
					Codec.BOOL.fieldOf("teleports_on_entity_hit").forGetter(TeleportOnHitEffect::teleportsOnEntityHit))
			.apply(instance, TeleportOnHitEffect::new));

	public static void setValues(MutableBoolean teleportsOnBlockHit, MutableBoolean teleportsOnEntityHit, Iterable<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) -> {
				List<EnchantmentEffectEntry<TeleportOnHitEffect>> effects = enchantment.value().effects().get(ModEnchantmentEffectComponentTypes.TELEPORT_ON_HIT);
				if (effects != null) {
					effects.forEach(effect -> {
						teleportsOnBlockHit.setValue(teleportsOnBlockHit.booleanValue() || effect.effect().teleportsOnBlockHit());
						teleportsOnEntityHit.setValue(teleportsOnEntityHit.booleanValue() || effect.effect().teleportsOnEntityHit());
					});
				}
			});
		}
	}
}
