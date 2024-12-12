/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmententityeffecttype.freeze;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.WaterAnimalEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(SquidEntity.class)
public abstract class SquidEntityMixin extends WaterAnimalEntity {
	@Shadow
	private float thrustTimerSpeed;

	protected SquidEntityMixin(EntityType<? extends WaterAnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tickMovement", at = @At("HEAD"))
	private void enchancement$freeze(CallbackInfo ci) {
		if (ModEntityComponents.FROZEN.get(this).isFrozen()) {
			thrustTimerSpeed = 0;
		}
	}

	@ModifyArgs(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/SquidEntity;setVelocity(DDD)V", ordinal = 0))
	private void enchancement$freeze(Args args) {
		if (ModEntityComponents.FROZEN.get(this).isFrozen()) {
			args.set(0, getVelocity().getX());
			args.set(1, getVelocity().getY());
			args.set(2, getVelocity().getZ());
		}
	}
}
