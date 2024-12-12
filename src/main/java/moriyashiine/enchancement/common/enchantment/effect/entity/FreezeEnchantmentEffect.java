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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public record FreezeEnchantmentEffect(EnchantmentLevelBasedValue duration) implements EnchantmentEntityEffect {
	public static final MapCodec<FreezeEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					EnchantmentLevelBasedValue.CODEC.fieldOf("duration").forGetter(FreezeEnchantmentEffect::duration))
			.apply(instance, FreezeEnchantmentEffect::new));

	@Override
	public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
		if (user.canFreeze()) {
			int freezeTicks = MathHelper.floor(duration().getValue(level) * 20);
			if (user.getFrozenTicks() < freezeTicks) {
				user.setFrozenTicks(freezeTicks);
			}
		}
	}

	@Override
	public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
		return CODEC;
	}
}
