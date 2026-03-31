/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.teleportonhit;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.component.entity.TeleportOnHitComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow {
	protected ThrownTridentMixin(EntityType<? extends AbstractArrow> type, Level level) {
		super(type, level);
	}

	@Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V", at = @At("TAIL"))
	private void enchancement$teleportOnHit(Level level, LivingEntity owner, ItemStack tridentItem, CallbackInfo ci) {
		TeleportOnHitComponent.maybeSet(owner, tridentItem, this);
	}

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/arrow/ThrownTrident;isNoPhysics()Z"))
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
