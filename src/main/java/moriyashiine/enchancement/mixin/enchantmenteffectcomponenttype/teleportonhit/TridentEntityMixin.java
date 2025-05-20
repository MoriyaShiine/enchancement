/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.teleportonhit;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.component.entity.TeleportOnHitComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {
	protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
	private void enchancement$teleportOnHit(World world, LivingEntity owner, ItemStack stack, CallbackInfo ci) {
		TeleportOnHitComponent.maybeSet(owner, stack, this);
	}

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;isNoClip()Z"))
	private boolean enchancement$rebalanceEquipment(boolean value) {
		TeleportOnHitComponent teleportOnHitComponent = ModEntityComponents.TELEPORT_ON_HIT.get(this);
		if (teleportOnHitComponent.teleportsOnBlockHit() || teleportOnHitComponent.teleportsOnEntityHit()) {
			if (getOwner() instanceof LivingEntity living && living.hurtTime != 0) {
				return true;
			}
		}
		return value;
	}
}
