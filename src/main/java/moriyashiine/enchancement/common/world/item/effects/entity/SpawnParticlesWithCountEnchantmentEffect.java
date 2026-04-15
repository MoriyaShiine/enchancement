/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.item.effects.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.SpawnParticlesEffect;
import net.minecraft.world.phys.Vec3;

public record SpawnParticlesWithCountEnchantmentEffect(SpawnParticlesEffect effect, LevelBasedValue count) implements EnchantmentEntityEffect {
	public static final MapCodec<SpawnParticlesWithCountEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					SpawnParticlesEffect.CODEC.fieldOf("effect").forGetter(SpawnParticlesWithCountEnchantmentEffect::effect),
					LevelBasedValue.CODEC.fieldOf("count").forGetter(SpawnParticlesWithCountEnchantmentEffect::count))
			.apply(instance, SpawnParticlesWithCountEnchantmentEffect::new));

	public static int countOverride = -1;

	@Override
	public MapCodec<SpawnParticlesWithCountEnchantmentEffect> codec() {
		return CODEC;
	}

	@Override
	public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
		countOverride = Mth.floor(count().calculate(enchantmentLevel));
		effect().apply(serverLevel, enchantmentLevel, item, entity, position);
		countOverride = -1;
	}
}
