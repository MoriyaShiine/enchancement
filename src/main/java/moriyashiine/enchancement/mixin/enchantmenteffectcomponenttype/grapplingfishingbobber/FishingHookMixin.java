/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.grapplingfishingbobber;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.StrengthHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin extends Projectile implements StrengthHolder {
	@Unique
	private static final EntityDataAccessor<Float> STRENGTH = SynchedEntityData.defineId(FishingHookMixin.class, EntityDataSerializers.FLOAT);

	@Unique
	private Vec3 grapplePos = null;
	@Unique
	private BlockPos grappleBlockPos = null;
	@Unique
	private BlockState grappleState = null;

	@Shadow
	public abstract @Nullable Player getPlayerOwner();

	public FishingHookMixin(EntityType<? extends Projectile> type, Level level) {
		super(type, level);
	}

	@Override
	public void enchancement$setStrength(float strength) {
		entityData.set(STRENGTH, strength);
	}

	@Unique
	private float getStrength() {
		return entityData.get(STRENGTH);
	}

	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	private void enchancement$grappleFishingBobber(SynchedEntityData.Builder entityData, CallbackInfo ci) {
		entityData.define(STRENGTH, 0F);
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/FishingHook;checkCollision()V"), cancellable = true)
	private void enchancement$grappleFishingBobber(CallbackInfo ci) {
		if (getStrength() != 0) {
			if (grappleState != null) {
				setPos(grapplePos);
				setDeltaMovement(Vec3.ZERO);
				if (tickCount % 10 == 0 && level().getBlockState(grappleBlockPos) != grappleState) {
					grapplePos = null;
					grappleBlockPos = null;
					grappleState = null;
				}
				ci.cancel();
			} else {
				double hX = getDeltaMovement().multiply(1, 0, 1).length();
				if (hX == 0) {
					for (Direction direction : Direction.values()) {
						Vec3 offset = position().relative(direction, 0.2);
						BlockHitResult result = level().isBlockInLine(new ClipBlockStateContext(position(), offset, state -> !state.canBeReplaced()));
						if (!level().getBlockState(result.getBlockPos()).canBeReplaced()) {
							onHitBlock(result);
							return;
						}
					}
				}
			}
		}
	}

	@Inject(method = "shouldStopFishing", at = @At("HEAD"), cancellable = true)
	private void enchancement$grappleFishingBobber(Player owner, CallbackInfoReturnable<Boolean> cir) {
		if (getStrength() != 0) {
			if (owner.isRemoved() || !owner.isAlive() || !owner.getMainHandItem().is(Items.FISHING_ROD) && !owner.getOffhandItem().is(Items.FISHING_ROD) || distanceToSqr(owner) > 4096) {
				discard();
				cir.setReturnValue(true);
			}
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "onHitBlock", at = @At("TAIL"))
	private void enchancement$grappleFishingBobber(BlockHitResult hitResult, CallbackInfo ci) {
		if (getStrength() != 0 && getPlayerOwner() instanceof Player player) {
			grapplePos = hitResult.getLocation().relative(hitResult.getDirection(), 0.01);
			grappleBlockPos = hitResult.getBlockPos();
			grappleState = level().getBlockState(grappleBlockPos);
			setPos(grapplePos);
			setDeltaMovement(Vec3.ZERO);
			if (level().isClientSide()) {
				player.playSound(ModSoundEvents.FISHING_BOBBER_GRAPPLE, 1, 1);
			}
		}
	}

	@Inject(method = "onHitEntity", at = @At("TAIL"))
	private void enchancement$grappleFishingBobber(EntityHitResult hitResult, CallbackInfo ci) {
		if (getStrength() != 0 && level().isClientSide()) {
			Player player = getPlayerOwner();
			if (player != null) {
				player.playSound(ModSoundEvents.FISHING_BOBBER_GRAPPLE, 1, 1);
			}
		}
	}

	@ModifyArg(method = "pullEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;scale(D)Lnet/minecraft/world/phys/Vec3;"))
	private double enchancement$grappleFishingBobber(double value) {
		if (getStrength() != 0) {
			return value * getStrength();
		}
		return value;
	}

	@ModifyReturnValue(method = "retrieve", at = @At("RETURN"))
	private int enchancement$grappleFishingBobber(int original) {
		if (getStrength() != 0) {
			if (grappleState != null) {
				Player player = getPlayerOwner();
				if (player != null) {
					if (!level().isClientSide()) {
						if (getY() > player.getY()) {
							player.setDeltaMovement(player.getDeltaMovement().x(), 0, player.getDeltaMovement().z());
						}
						player.setDeltaMovement(player.getDeltaMovement().add(new Vec3(clamp(getX() - player.getX()), clamp(getY() - player.getY()), clamp(getZ() - player.getZ())).scale(0.2)));
						player.hurtMarked = true;
					}
				}
			}
			return level().isClientSide() ? original : 1;
		}
		return original;
	}

	@Unique
	private double clamp(double value) {
		float cap = getStrength() * 4;
		return Mth.clamp(value, -cap, cap);
	}
}
