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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record SmashEffect(EnchantmentLevelBasedValue strength) implements EnchantmentEntityEffect {
	public static final MapCodec<SmashEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					EnchantmentLevelBasedValue.CODEC.fieldOf("strength").forGetter(SmashEffect::strength))
			.apply(instance, SmashEffect::new));

	@Override
	public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
		user.addVelocity(0, -strength().getValue(level), 0);
		user.velocityModified = true;
	}

	@Override
	public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
		return CODEC;
	}
}
