/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.grapple;

import moriyashiine.enchancement.common.entity.projectile.GrappleFishingBobberEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin extends ProjectileEntity {
	public FishingBobberEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@SuppressWarnings("ConstantValue")
	@Inject(method = "removeIfInvalid", at = @At("HEAD"), cancellable = true)
	private void enchancement$grapple(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
		if (FishingBobberEntity.class.cast(this) instanceof GrappleFishingBobberEntity) {
			if (player.isRemoved() || !player.isAlive() || !player.getMainHandStack().isOf(Items.FISHING_ROD) && !player.getOffHandStack().isOf(Items.FISHING_ROD) || this.squaredDistanceTo(player) > 4096) {
				discard();
				cir.setReturnValue(true);
			}
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;checkForCollision()V", shift = At.Shift.BEFORE), cancellable = true)
	private void enchancement$grapple(CallbackInfo ci) {
		if (FishingBobberEntity.class.cast(this) instanceof GrappleFishingBobberEntity grappleFishingBobber) {
			if (grappleFishingBobber.grappleState != null) {
				if (grappleFishingBobber.age % 10 == 0 && grappleFishingBobber.getWorld().getBlockState(grappleFishingBobber.grapplePos) != grappleFishingBobber.grappleState) {
					grappleFishingBobber.grapplePos = null;
					grappleFishingBobber.grappleState = null;
				}
				grappleFishingBobber.setVelocity(Vec3d.ZERO);
				ci.cancel();
			}
		}
	}
}
