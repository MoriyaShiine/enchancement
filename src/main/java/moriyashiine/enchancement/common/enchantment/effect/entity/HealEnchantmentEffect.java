/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.enchantment.effect.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record HealEnchantmentEffect(EnchantmentLevelBasedValue amount) implements EnchantmentEntityEffect {
	public static final MapCodec<HealEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					EnchantmentLevelBasedValue.CODEC.fieldOf("amount").forGetter(HealEnchantmentEffect::amount))
			.apply(instance, HealEnchantmentEffect::new));

	@Override
	public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
		if (user instanceof LivingEntity living) {
			living.heal(amount().getValue(level));
		}
	}

	@Override
	public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
		return CODEC;
	}
}
