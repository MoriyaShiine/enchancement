/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.bounce;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Unique
	private static final BlockStateParticleEffect SLIME_PARTICLE = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.getDefaultState());

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@SuppressWarnings("unchecked")
	@ModifyArg(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I"))
	private <T extends ParticleEffect> T enchancement$bounce(T value) {
		if (EnchancementUtil.hasAnyEnchantmentsWith(this, ModEnchantmentEffectComponentTypes.BOUNCE)) {
			return (T) SLIME_PARTICLE;
		}
		return value;
	}

	@ModifyExpressionValue(method = "travelControlled", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;canMoveVoluntarily()Z"))
	private boolean enchancement$bounce(boolean original) {
		return original || ModEntityComponents.BOUNCE.get(this).justBounced();
	}
}
