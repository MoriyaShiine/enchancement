/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.enchantment.effect.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.enchantment.effect.entity.SpawnParticlesEnchantmentEffect;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public record SpawnParticlesWithCountEnchantmentEffect(SpawnParticlesEnchantmentEffect effect,
													   EnchantmentLevelBasedValue count) implements EnchantmentEntityEffect {
	public static final MapCodec<SpawnParticlesWithCountEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					SpawnParticlesEnchantmentEffect.CODEC.fieldOf("effect").forGetter(SpawnParticlesWithCountEnchantmentEffect::effect),
					EnchantmentLevelBasedValue.CODEC.fieldOf("count").forGetter(SpawnParticlesWithCountEnchantmentEffect::count))
			.apply(instance, SpawnParticlesWithCountEnchantmentEffect::new));

	public static int countOverride = -1;

	@Override
	public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
		countOverride = MathHelper.floor(count().getValue(level));
		effect().apply(world, level, context, user, pos);
		countOverride = -1;
	}

	@Override
	public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
		return CODEC;
	}
}
