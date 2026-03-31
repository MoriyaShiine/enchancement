/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record FreezeEnchantmentEffect(LevelBasedValue duration) implements EnchantmentEntityEffect {
	public static final MapCodec<FreezeEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					LevelBasedValue.CODEC.fieldOf("duration").forGetter(FreezeEnchantmentEffect::duration))
			.apply(instance, FreezeEnchantmentEffect::new));

	@Override
	public MapCodec<FreezeEnchantmentEffect> codec() {
		return CODEC;
	}

	@Override
	public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
		if (entity.canFreeze()) {
			int freezeTicks = Mth.floor(duration().calculate(enchantmentLevel) * 20);
			if (entity.getTicksFrozen() < freezeTicks) {
				entity.setTicksFrozen(freezeTicks);
			}
		}
	}
}
