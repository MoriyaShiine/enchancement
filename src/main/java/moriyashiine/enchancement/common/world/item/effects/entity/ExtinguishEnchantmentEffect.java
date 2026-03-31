/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects.entity;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public class ExtinguishEnchantmentEffect implements EnchantmentEntityEffect {
	public static final ExtinguishEnchantmentEffect INSTANCE = new ExtinguishEnchantmentEffect();
	public static final MapCodec<ExtinguishEnchantmentEffect> CODEC = MapCodec.unit(INSTANCE);

	private ExtinguishEnchantmentEffect() {
	}

	@Override
	public MapCodec<ExtinguishEnchantmentEffect> codec() {
		return CODEC;
	}

	@Override
	public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
		if (entity.isOnFire()) {
			entity.extinguishFire();
		}
	}
}
