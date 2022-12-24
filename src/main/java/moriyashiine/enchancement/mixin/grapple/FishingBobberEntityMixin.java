/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.grapple;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin extends Entity {
	@Unique
	private static final TrackedData<Boolean> HAS_GRAPPLE = DataTracker.registerData(FishingBobberEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	@Unique
	private BlockPos grapplePos = null;
	@Unique
	private BlockState grappleState = null;

	@Shadow
	public abstract @Nullable
	PlayerEntity getPlayerOwner();

	public FishingBobberEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyVariable(method = "<init>(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;II)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/Vec3d;multiply(DDD)Lnet/minecraft/util/math/Vec3d;"))
	private Vec3d enchancement$grapple(Vec3d value, PlayerEntity player) {
		getDataTracker().set(HAS_GRAPPLE, EnchancementUtil.hasEnchantment(ModEnchantments.GRAPPLE, player));
		if (hasGrapple()) {
			return value.multiply(2, 2, 2);
		}
		return value;
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;checkForCollision()V", shift = At.Shift.BEFORE), cancellable = true)
	private void enchancement$grapple(CallbackInfo ci) {
		if (hasGrapple() && grappleState != null) {
			if (age % 10 == 0 && world.getBlockState(grapplePos) != grappleState) {
				grapplePos = null;
				grappleState = null;
			}
			setVelocity(Vec3d.ZERO);
			ci.cancel();
		}
	}

	@Inject(method = "onBlockHit", at = @At("TAIL"))
	private void enchancement$grapple(BlockHitResult blockHitResult, CallbackInfo ci) {
		if (hasGrapple()) {
			grapplePos = blockHitResult.getBlockPos();
			grappleState = world.getBlockState(grapplePos);
		}
	}

	@ModifyArg(method = "pullHookedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;add(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;"))
	private Vec3d enchancement$grapple(Vec3d value) {
		if (hasGrapple()) {
			return value.multiply(2);
		}
		return value;
	}

	@Inject(method = "use", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
	private void enchancement$grapple(ItemStack usedItem, CallbackInfoReturnable<Integer> cir) {
		if (hasGrapple()) {
			int value = cir.getReturnValueI();
			if (grappleState != null) {
				PlayerEntity player = getPlayerOwner();
				if (player != null) {
					player.setVelocity(player.getVelocity().add(new Vec3d(Math.min(10, getX() - player.getX()), Math.min(10, getY() - player.getY()), Math.min(10, getZ() - player.getZ())).multiply(0.2)));
					player.velocityModified = true;
					value = 1;
				}
			}
			if (value > 0) {
				cir.setReturnValue(1);
			}
		}
	}

	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void enchancement$grappleRegisterDataTracker(CallbackInfo ci) {
		getDataTracker().startTracking(HAS_GRAPPLE, false);
	}

	@Unique
	private boolean hasGrapple() {
		return getDataTracker().get(HAS_GRAPPLE);
	}
}
