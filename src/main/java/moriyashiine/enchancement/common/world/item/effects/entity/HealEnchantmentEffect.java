/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record HealEnchantmentEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
	public static final MapCodec<HealEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					LevelBasedValue.CODEC.fieldOf("amount").forGetter(HealEnchantmentEffect::amount))
			.apply(instance, HealEnchantmentEffect::new));

	@Override
	public MapCodec<HealEnchantmentEffect> codec() {
		return CODEC;
	}

	@Override
	public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
		if (entity instanceof LivingEntity living) {
			living.heal(amount().calculate(enchantmentLevel));
		}
	}
}
