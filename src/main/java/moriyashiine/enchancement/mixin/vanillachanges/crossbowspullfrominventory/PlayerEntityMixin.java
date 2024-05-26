/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.vanillachanges.crossbowspullfrominventory;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Predicate;

@Mixin(value = PlayerEntity.class, priority = 1001)
public class PlayerEntityMixin {
	@Unique
	private static Predicate<ItemStack> heldItemsPredicate = null;

	@ModifyVariable(method = "getProjectileType", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/RangedWeaponItem;getHeldProjectiles()Ljava/util/function/Predicate;"))
	private Predicate<ItemStack> enchancement$crossbowsPullFromInventory(Predicate<ItemStack> value) {
		if (ModConfig.crossbowsPullFromInventory) {
			heldItemsPredicate = value;
		}
		return value;
	}

	@ModifyVariable(method = "getProjectileType", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/RangedWeaponItem;getProjectiles()Ljava/util/function/Predicate;"))
	private Predicate<ItemStack> enchancement$crossbowsPullFromInventory(Predicate<ItemStack> value, ItemStack stack) {
		if (ModConfig.crossbowsPullFromInventory) {
			value = value.or(heldItemsPredicate);
			heldItemsPredicate = null;
		}
		return value;
	}
}
