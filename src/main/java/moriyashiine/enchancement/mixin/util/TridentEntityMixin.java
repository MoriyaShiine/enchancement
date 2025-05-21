/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.util;

import moriyashiine.enchancement.common.component.entity.OwnedTridentComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public class TridentEntityMixin {
	@Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
	private void enchancement$markTridentOwner(World world, LivingEntity owner, ItemStack stack, CallbackInfo ci) {
		if (owner instanceof PlayerEntity player) {
			OwnedTridentComponent ownedTridentComponent = ModEntityComponents.OWNED_TRIDENT.get(this);
			ownedTridentComponent.markPlayerOwned(player.getActiveHand() == Hand.OFF_HAND ? PlayerInventory.OFF_HAND_SLOT : player.getInventory().getSelectedSlot());
			ownedTridentComponent.sync();
		}
	}
}
