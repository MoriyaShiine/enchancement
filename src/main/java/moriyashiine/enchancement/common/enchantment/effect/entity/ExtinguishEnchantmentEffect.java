/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.enchantment.effect.entity;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class ExtinguishEnchantmentEffect implements EnchantmentEntityEffect {
	public static final ExtinguishEnchantmentEffect INSTANCE = new ExtinguishEnchantmentEffect();
	public static final MapCodec<ExtinguishEnchantmentEffect> CODEC = MapCodec.unit(INSTANCE);

	private ExtinguishEnchantmentEffect() {
	}

	@Override
	public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
		if (user.isOnFire()) {
			user.extinguishWithSound();
		}
	}

	@Override
	public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
		return CODEC;
	}
}
