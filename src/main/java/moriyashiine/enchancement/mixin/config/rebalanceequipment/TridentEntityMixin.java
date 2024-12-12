/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {
	@Shadow
	public abstract boolean isOwnerAlive();

	protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
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
