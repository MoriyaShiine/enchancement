/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.bounce;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Unique
	private static final BlockParticleOption SLIME_PARTICLE = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState());

	public LivingEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@SuppressWarnings("unchecked")
	@ModifyArg(method = "checkFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"))
	private <T extends ParticleOptions> T enchancement$bounce(T value) {
		if (EnchancementUtil.hasAnyEnchantmentsWith(this, ModEnchantmentEffectComponentTypes.BOUNCE)) {
			return (T) SLIME_PARTICLE;
		}
		return value;
	}

	@ModifyExpressionValue(method = "travelRidden", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canSimulateMovement()Z"))
	private boolean enchancement$bounce(boolean original) {
		return original || ModEntityComponents.BOUNCE.get(this).justBounced();
	}
}
