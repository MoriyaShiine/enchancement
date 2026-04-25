/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.ExtendedWaterTimeComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record SetExtendedWaterTimeEffect(LevelBasedValue duration) implements EnchantmentEntityEffect {
	public static final MapCodec<SetExtendedWaterTimeEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					LevelBasedValue.CODEC.fieldOf("duration").forGetter(SetExtendedWaterTimeEffect::duration))
			.apply(instance, SetExtendedWaterTimeEffect::new));

	@Override
	public MapCodec<SetExtendedWaterTimeEffect> codec() {
		return CODEC;
	}

	@Override
	public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
		ExtendedWaterTimeComponent extendedWaterTimeComponent = ModEntityComponents.EXTENDED_WATER_TIME.getNullable(entity);
		if (extendedWaterTimeComponent != null) {
			extendedWaterTimeComponent.markWet(Mth.floor(duration().calculate(enchantmentLevel) * 20));
			extendedWaterTimeComponent.sync();
		}
	}
}
