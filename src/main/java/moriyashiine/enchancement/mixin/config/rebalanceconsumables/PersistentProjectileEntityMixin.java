/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceconsumables;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {
	@Shadow
	protected abstract ItemStack asItemStack();

	@Shadow
	public PersistentProjectileEntity.PickupPermission pickupType;

	public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"))
	private void enchancement$rebalanceConsumables(EntityHitResult entityHitResult, CallbackInfo ci) {
		if (shouldApply()) {
			Entity entity = entityHitResult.getEntity();
			if (entity instanceof EnderDragonPart part) {
				entity = part.owner;
			}
			if (entity instanceof LivingEntity living) {
				ItemStack stack = asItemStack();
				if (stack.isIn(ItemTags.ARROWS)) {
					ItemEntity drop = living.dropStack(stack, 1);
					if (drop != null) {
						drop.setOwner(getOwner().getUuid());
					}
				}
			}
		}
	}

	@WrapWithCondition(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setStuckArrowCount(I)V"))
	private boolean enchancement$rebalanceConsumables(LivingEntity instance, int stuckArrowCount) {
		return !shouldApply();
	}

	@Unique
	private boolean shouldApply() {
		return ModConfig.rebalanceConsumables && pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED && getOwner() instanceof PlayerEntity;
	}
}
