/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.allowloadingprojectile;

import moriyashiine.enchancement.common.enchantment.effect.AllowLoadingProjectileEffect;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Set;
import java.util.function.Predicate;

@Mixin({PlayerEntity.class, HostileEntity.class})
public class CrossbowMixin {
	@ModifyVariable(method = "getProjectileType", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/RangedWeaponItem;getHeldProjectiles()Ljava/util/function/Predicate;"))
	private Predicate<ItemStack> enchancement$allowLoadingProjectile(Predicate<ItemStack> value, ItemStack stack) {
		Set<Item> items = AllowLoadingProjectileEffect.getItems(stack);
		for (Item item : items) {
			value = value.or(projectile -> projectile.isOf(item));
		}
		return value;
	}
}
