/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.enchantment.effect.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.component.entity.ExtendedWaterTimeComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public record SetExtendedWaterTimeEffect(
		EnchantmentLevelBasedValue duration) implements EnchantmentEntityEffect {
	public static final MapCodec<SetExtendedWaterTimeEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					EnchantmentLevelBasedValue.CODEC.fieldOf("duration").forGetter(SetExtendedWaterTimeEffect::duration))
			.apply(instance, SetExtendedWaterTimeEffect::new));

	@Override
	public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
		ExtendedWaterTimeComponent extendedWaterTimeComponent = ModEntityComponents.EXTENDED_WATER_TIME.getNullable(user);
		if (extendedWaterTimeComponent != null) {
			extendedWaterTimeComponent.markWet(MathHelper.floor(duration().getValue(level) * 20));
			extendedWaterTimeComponent.sync();
		}
	}

	@Override
	public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
		return CODEC;
	}
}
