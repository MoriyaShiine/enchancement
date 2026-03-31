/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmententityeffecttype.freeze;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AgeableWaterCreature;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Squid.class)
public abstract class SquidMixin extends AgeableWaterCreature {
	@Shadow
	private float tentacleSpeed;

	protected SquidMixin(EntityType<? extends AgeableWaterCreature> type, Level level) {
		super(type, level);
	}

	@Inject(method = "aiStep", at = @At("HEAD"))
	private void enchancement$freeze(CallbackInfo ci) {
		if (ModEntityComponents.FROZEN.get(this).isFrozen()) {
			tentacleSpeed = 0;
		}
	}

	@ModifyArgs(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/squid/Squid;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", ordinal = 0))
	private void enchancement$freeze(Args args) {
		if (ModEntityComponents.FROZEN.get(this).isFrozen()) {
			args.set(0, getDeltaMovement().x());
			args.set(1, getDeltaMovement().y());
			args.set(2, getDeltaMovement().z());
		}
	}
}
