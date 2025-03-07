/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.criticaltipper;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.payload.AddEmitterParticlePayload;
import moriyashiine.enchancement.common.enchantment.effect.CriticalTipperEffect;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Unique
	private ParticleType<?> particleType = null;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 2)
	private boolean enchancement$criticalTipper(boolean value, Entity target, @Local(ordinal = 2) float attackCooldownProgress) {
		if (forceCritical(target, attackCooldownProgress)) {
			particleType = CriticalTipperEffect.getParticleType(getMainHandStack());
			return true;
		}
		return value;
	}

	@WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addCritParticles(Lnet/minecraft/entity/Entity;)V"))
	private void enchancement$criticalTipper(PlayerEntity instance, Entity target, Operation<Void> original) {
		if (particleType != null) {
			if (!target.getWorld().isClient) {
				PlayerLookup.tracking(target).forEach(foundPlayer -> AddEmitterParticlePayload.send(foundPlayer, target, particleType));
				if (target instanceof ServerPlayerEntity player) {
					AddEmitterParticlePayload.send(player, target, particleType);
				}
			}
			particleType = null;
		} else {
			original.call(instance, target);
		}
	}

	@Unique
	private boolean forceCritical(Entity target, float attackCooldownProgress) {
		if (EnchantmentHelper.hasAnyEnchantmentsWith(getMainHandStack(), ModEnchantmentEffectComponentTypes.CRITICAL_TIPPER) && attackCooldownProgress > 0.9F) {
			float distanceLeniency = CriticalTipperEffect.getDistanceLeniency(getMainHandStack(), getRandom());
			return distanceTo(target) > getAttributeValue(EntityAttributes.ENTITY_INTERACTION_RANGE) - distanceLeniency;
		}
		return false;
	}
}
