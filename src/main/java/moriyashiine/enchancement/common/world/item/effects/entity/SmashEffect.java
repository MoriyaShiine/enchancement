/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record SmashEffect(LevelBasedValue strength) implements EnchantmentEntityEffect {
	public static final MapCodec<SmashEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					LevelBasedValue.CODEC.fieldOf("strength").forGetter(SmashEffect::strength))
			.apply(instance, SmashEffect::new));

	@Override
	public MapCodec<SmashEffect> codec() {
		return CODEC;
	}

	@Override
	public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
		entity.push(0, -strength().calculate(enchantmentLevel), 0);
		entity.hurtMarked = true;
	}
}
