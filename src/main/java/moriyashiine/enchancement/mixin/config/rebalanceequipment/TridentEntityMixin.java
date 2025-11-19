/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.component.entity.OwnedTridentComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {
	@Shadow
	public abstract boolean isOwnerAlive();

	protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
	private void enchancement$rebalanceEquipment(World world, LivingEntity owner, ItemStack stack, CallbackInfo ci) {
		if (ModConfig.rebalanceEquipment && owner instanceof PlayerEntity player) {
			OwnedTridentComponent ownedTridentComponent = ModEntityComponents.OWNED_TRIDENT.get(this);
			ownedTridentComponent.markPlayerOwned(player.getActiveHand() == Hand.OFF_HAND ? PlayerInventory.OFF_HAND_SLOT : player.getInventory().getSelectedSlot());
			ownedTridentComponent.sync();
		}
	}

	@ModifyVariable(method = "age", at = @At("STORE"))
	private int enchancement$rebalanceEquipmentPreventDespawn(int value) {
		return ModConfig.rebalanceEquipment && ModEntityComponents.OWNED_TRIDENT.get(this).isOwnedByPlayer() ? 1 : value;
	}

	@ModifyVariable(method = "tick", at = @At("STORE"))
	private int enchancement$rebalanceEquipment(int value) {
		if (ModConfig.rebalanceEquipment && value > 0 && !isOwnerAlive()) {
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
		if (ModConfig.rebalanceEquipment && getY() <= getEntityWorld().getBottomY()) {
			return true;
		}
		return value;
	}
}
