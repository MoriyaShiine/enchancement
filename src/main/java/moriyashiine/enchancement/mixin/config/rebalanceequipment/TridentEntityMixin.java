/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {
	@Unique
	private boolean ownedByPlayer = false;

	@Shadow
	public abstract boolean isOwnerAlive();

	protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void enchancement$rebalanceEquipment(CallbackInfo ci) {
		if (age % 20 == 0 && !ownedByPlayer && getOwner() instanceof PlayerEntity) {
			ownedByPlayer = true;
		}
	}

	@ModifyVariable(method = "age", at = @At("STORE"))
	private int enchancement$rebalanceEquipmentPreventDespawn(int value) {
		return ownedByPlayer ? 1 : value;
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void enchancement$rebalanceEquipmentRead(NbtCompound nbt, CallbackInfo ci) {
		ownedByPlayer = nbt.getBoolean("OwnedByPlayer", false);
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void enchancement$rebalanceEquipmentWrite(NbtCompound nbt, CallbackInfo ci) {
		nbt.putBoolean("OwnedByPlayer", ownedByPlayer);
	}

	@ModifyVariable(method = "tick", at = @At("STORE"))
	private int enchancement$rebalanceEquipment(int value) {
		if (value > 0 && !isOwnerAlive()) {
			if (getVelocity().length() > 0) {
				setVelocity(getVelocity().multiply(0.9));
			} else {
				setVelocity(Vec3d.ZERO);
			}
			return 0;
		}
		return value;
	}

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;isNoClip()Z"))
	private boolean enchancement$rebalanceEquipment(boolean value) {
		if (ModConfig.rebalanceEquipment && getY() <= getWorld().getBottomY()) {
			return true;
		}
		return value;
	}
}
