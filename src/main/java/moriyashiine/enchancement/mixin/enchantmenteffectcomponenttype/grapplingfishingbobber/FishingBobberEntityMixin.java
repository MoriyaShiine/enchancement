/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.grapplingfishingbobber;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.entity.projectile.StrengthHolder;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin extends ProjectileEntity implements StrengthHolder {
	@Unique
	private BlockPos grapplePos = null;
	@Unique
	private BlockState grappleState = null;
	@Unique
	private static final TrackedData<Float> STRENGTH = DataTracker.registerData(FishingBobberEntityMixin.class, TrackedDataHandlerRegistry.FLOAT);

	@Shadow
	public abstract @Nullable PlayerEntity getPlayerOwner();

	public FishingBobberEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void enchancement$setStrength(float strength) {
		dataTracker.set(STRENGTH, strength);
	}

	@Unique
	private float getStrength() {
		return dataTracker.get(STRENGTH);
	}

	@Inject(method = "<init>(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;IILnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
	private void enchancement$grappleFishingBobber(PlayerEntity thrower, World world, int luckBonus, int waitTimeReductionTicks, ItemStack stack, CallbackInfo ci) {
		if (world instanceof ServerWorld serverWorld) {
			float grapplingStrength = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.GRAPPLING_FISHING_BOBBER, serverWorld, stack, 0);
			if (grapplingStrength != 0) {
				enchancement$setStrength(grapplingStrength);
			}
		}
	}

	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void enchancement$grappleFishingBobber(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(STRENGTH, 0F);
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;checkForCollision()V"), cancellable = true)
	private void enchancement$grappleFishingBobber(CallbackInfo ci) {
		if (getStrength() != 0) {
			if (grappleState != null) {
				if (age % 10 == 0 && getWorld().getBlockState(grapplePos) != grappleState) {
					grapplePos = null;
					grappleState = null;
				}
				setVelocity(Vec3d.ZERO);
				ci.cancel();
			}
		}
	}

	@Inject(method = "removeIfInvalid", at = @At("HEAD"), cancellable = true)
	private void enchancement$grappleFishingBobber(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
		if (getStrength() != 0) {
			if (player.isRemoved() || !player.isAlive() || !player.getMainHandStack().isOf(Items.FISHING_ROD) && !player.getOffHandStack().isOf(Items.FISHING_ROD) || squaredDistanceTo(player) > 4096) {
				discard();
				cir.setReturnValue(true);
			}
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "onBlockHit", at = @At("TAIL"))
	private void enchancement$grappleFishingBobber(BlockHitResult blockHitResult, CallbackInfo ci) {
		if (getStrength() != 0) {
			grapplePos = blockHitResult.getBlockPos();
			grappleState = getWorld().getBlockState(grapplePos);
			if (getWorld().isClient) {
				PlayerEntity owner = getPlayerOwner();
				if (owner != null) {
					owner.playSound(ModSoundEvents.ENTITY_FISHING_BOBBER_GRAPPLE, 1, 1);
				}
			}
		}
	}

	@Inject(method = "onEntityHit", at = @At("TAIL"))
	private void enchancement$grappleFishingBobber(EntityHitResult entityHitResult, CallbackInfo ci) {
		if (getStrength() != 0 && getWorld().isClient) {
			PlayerEntity owner = getPlayerOwner();
			if (owner != null) {
				owner.playSound(ModSoundEvents.ENTITY_FISHING_BOBBER_GRAPPLE, 1, 1);
			}
		}
	}

	@WrapOperation(method = "pullHookedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
	private void enchancement$grappleFishingBobber(Entity instance, Vec3d velocity, Operation<Void> original) {
		if (getStrength() != 0) {
			velocity = velocity.multiply(getStrength());
			if (!instance.isOnGround() && EnchancementUtil.hasAnyEnchantmentsWith(instance, ModEnchantmentEffectComponentTypes.BOUNCE)) {
				instance.fallDistance += 6;
			}
		}
		original.call(instance, velocity);
	}

	@ModifyReturnValue(method = "use", at = @At("RETURN"))
	private int enchancement$grappleFishingBobber(int original) {
		if (getStrength() != 0) {
			if (grappleState != null) {
				PlayerEntity player = getPlayerOwner();
				if (player != null) {
					if (!getWorld().isClient) {
						if (getY() > player.getY()) {
							player.setVelocity(player.getVelocity().getX(), 0, player.getVelocity().getZ());
						}
						player.setVelocity(player.getVelocity().add(new Vec3d(Math.min(getStrength() * 4, getX() - player.getX()), Math.min(getStrength() * 4, getY() - player.getY()), Math.min(getStrength() * 4, getZ() - player.getZ())).multiply(0.2)));
						player.velocityModified = true;
					}
					if (!player.isOnGround() && EnchancementUtil.hasAnyEnchantmentsWith(player, ModEnchantmentEffectComponentTypes.BOUNCE)) {
						player.fallDistance += 6;
					}
				}
			}
			return getWorld().isClient ? original : 1;
		}
		return original;
	}
}
