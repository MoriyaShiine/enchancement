/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmententityeffecttype.freeze;

import moriyashiine.enchancement.common.component.entity.FrozenComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, priority = 1001)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
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

	@Inject(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;sendEntityStatus(Lnet/minecraft/entity/Entity;B)V"), cancellable = true)
	private void enchancement$freeze(DamageSource damageSource, CallbackInfo ci) {
		if (ModEntityComponents.FROZEN.get(this).shouldFreezeOnDeath(damageSource)) {
			ci.cancel();
		}
	}

	@Inject(method = "pushAwayFrom", at = @At("HEAD"))
	private void enchancement$freeze(Entity entity, CallbackInfo ci) {
		if (getEntityWorld() instanceof ServerWorld world) {
			FrozenComponent frozenComponent = ModEntityComponents.FROZEN.get(this);
			if (frozenComponent.isFrozen()) {
				Entity lastFreezingAttacker = frozenComponent.getLastFreezingAttacker();
				if (SLibUtils.shouldHurt(lastFreezingAttacker, entity) && entity.damage(world, world.getDamageSources().create(DamageTypes.FREEZE, lastFreezingAttacker == null ? this : lastFreezingAttacker), 8)) {
					damage(world, getDamageSources().generic(), 2);
					entity.setFrozenTicks(800);
				}
			}
		}
	}
}
