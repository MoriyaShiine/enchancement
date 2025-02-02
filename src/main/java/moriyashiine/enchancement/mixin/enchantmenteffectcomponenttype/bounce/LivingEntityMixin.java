/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.bounce;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.common.component.entity.BounceComponent;
import moriyashiine.enchancement.common.event.BounceEvent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

	@Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
	private void enchancement$bounce(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		maybeBounce(damageSource, cir);
	}

	@WrapWithCondition(method = "travelControlled", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
	private boolean enchancement$bounce(LivingEntity instance, Vec3d vec3d) {
		return !ModEntityComponents.BOUNCE.get(this).justBounced();
	}

	@Unique
	protected void maybeBounce(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		if (!damageSource.isOf(DamageTypes.STALAGMITE) && fallDistance > getSafeFallDistance() && EnchancementUtil.hasAnyEnchantmentsWith(this, ModEnchantmentEffectComponentTypes.BOUNCE)) {
			getWorld().playSound(null, getX(), getY(), getZ(), SoundEvents.BLOCK_SLIME_BLOCK_FALL, getSoundCategory());
			BounceComponent bounceComponent = ModEntityComponents.BOUNCE.get(this);
			if (shouldBounce(bounceComponent)) {
				double bounceStrength = Math.log((fallDistance / 7) + 1) / Math.log(1.05) / 16;
				if (isPlayer() || isControlledByPlayer()) {
					BounceEvent.bounce(this, bounceComponent, bounceStrength);
				} else {
					BounceEvent.scheduleBounce(this, bounceStrength);
				}
			}
			cir.setReturnValue(false);
		}
	}

	@Unique
	private boolean shouldBounce(BounceComponent bounceComponent) {
		boolean bounce = !EnchancementUtil.isSneakingOrSitting(this, bypassesLandingEffects());
		if (bounceComponent.hasInvertedBounce()) {
			bounce = !bounce;
		}
		return bounce;
	}
}
