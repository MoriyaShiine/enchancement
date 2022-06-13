/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.acceleration;

import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Inject(method = "getMovementSpeed", at = @At("RETURN"), cancellable = true)
	private void enchancement$acceleration(CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue(cir.getReturnValueF() * ModEntityComponents.ACCELERATION.get(this).getSpeedMultiplier());
	}

	@ModifyArg(method = "increaseTravelMotionStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExhaustion(F)V", ordinal = 3))
	private float enchancement$acceleration(float value) {
		return value / ModEntityComponents.ACCELERATION.get(this).getSpeedMultiplier();
	}
}
