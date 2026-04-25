/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmententityeffecttype.freeze;

import moriyashiine.enchancement.common.component.entity.enchantmenteffecttype.FrozenComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, priority = 1001)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Inject(method = "isAlive", at = @At("HEAD"), cancellable = true)
	private void enchancement$freezeAlive(CallbackInfoReturnable<Boolean> cir) {
		if (ModEntityComponents.FROZEN.get(this).isFrozen()) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
	private void enchancement$freezePushable(CallbackInfoReturnable<Boolean> cir) {
		if (ModEntityComponents.FROZEN.get(this).isFrozen()) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"), cancellable = true)
	private void enchancement$freeze(DamageSource source, CallbackInfo ci) {
		if (ModEntityComponents.FROZEN.get(this).shouldFreezeOnDeath(source)) {
			ci.cancel();
		}
	}

	@Inject(method = "push", at = @At("HEAD"))
	private void enchancement$freeze(Entity entity, CallbackInfo ci) {
		if (level() instanceof ServerLevel level) {
			FrozenComponent frozenComponent = ModEntityComponents.FROZEN.get(this);
			if (frozenComponent.isFrozen()) {
				Entity lastFreezingAttacker = frozenComponent.getLastFreezingAttacker();
				if (SLibUtils.shouldHurt(lastFreezingAttacker, entity) && entity.hurtServer(level, level.damageSources().source(DamageTypes.FREEZE, lastFreezingAttacker == null ? this : lastFreezingAttacker), 8)) {
					hurtServer(level, damageSources().generic(), 2);
					entity.setTicksFrozen(800);
				}
			}
		}
	}
}
