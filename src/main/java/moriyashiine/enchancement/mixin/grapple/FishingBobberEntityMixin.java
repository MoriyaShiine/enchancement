/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.grapple;

import moriyashiine.enchancement.common.entity.projectile.GrappleFishingBobberEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin {
	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;checkForCollision()V", shift = At.Shift.BEFORE), cancellable = true)
	private void enchancement$grapple(CallbackInfo ci) {
		if (FishingBobberEntity.class.cast(this) instanceof GrappleFishingBobberEntity grappleFishingBobber) {
			if (grappleFishingBobber.grappleState != null) {
				if (grappleFishingBobber.age % 10 == 0 && grappleFishingBobber.world.getBlockState(grappleFishingBobber.grapplePos) != grappleFishingBobber.grappleState) {
					grappleFishingBobber.grapplePos = null;
					grappleFishingBobber.grappleState = null;
				}
				grappleFishingBobber.setVelocity(Vec3d.ZERO);
				ci.cancel();
			}
		}
	}
}
