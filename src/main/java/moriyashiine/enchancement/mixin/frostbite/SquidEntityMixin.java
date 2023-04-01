/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.frostbite;

import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(SquidEntity.class)
public class SquidEntityMixin extends WaterCreatureEntity {
	@Shadow
	private float thrustTimerSpeed;

	protected SquidEntityMixin(EntityType<? extends WaterCreatureEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tickMovement", at = @At("HEAD"))
	private void enchancement$frostbite(CallbackInfo ci) {
		if (ModEntityComponents.FROZEN.get(this).isFrozen()) {
			thrustTimerSpeed = 0;
		}
	}

	@ModifyArgs(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/SquidEntity;setVelocity(DDD)V", ordinal = 0))
	private void enchancement$frostbite(Args args) {
		if (ModEntityComponents.FROZEN.get(this).isFrozen()) {
			ModEntityComponents.FROZEN_SQUID.maybeGet(this).ifPresent(frozenSquidComponent -> {
				args.set(0, getVelocity().getX());
				args.set(1, getVelocity().getY());
				args.set(2, getVelocity().getZ());
			});
		}
	}
}
