/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {
	@SuppressWarnings("ConstantValue")
	@WrapOperation(method = "tryPickup", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"))
	private boolean enchancement$rebalanceEquipment(PlayerInventory instance, ItemStack stack, Operation<Boolean> original) {
		if (ModConfig.rebalanceEquipment && (Object) this instanceof TridentEntity) {
			int slot = ModEntityComponents.OWNED_TRIDENT.get(this).getSlot();
			if (instance.getStack(slot).isEmpty() && instance.insertStack(slot, stack)) {
				return true;
			}
		}
		return original.call(instance, stack);
	}
}
