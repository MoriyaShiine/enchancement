/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.allowloadingprojectile;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.enchantment.effect.AllowLoadingProjectileEffect;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;
import java.util.function.Predicate;

@Mixin({PlayerEntity.class, HostileEntity.class})
public class CrossbowMixin {
	@ModifyExpressionValue(method = "getProjectileType", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/RangedWeaponItem;getHeldProjectiles()Ljava/util/function/Predicate;"))
	private Predicate<ItemStack> enchancement$allowLoadingProjectile(Predicate<ItemStack> original, ItemStack stack) {
		Set<Item> items = AllowLoadingProjectileEffect.getItems(stack);
		for (Item item : items) {
			original = original.or(projectile -> projectile.isOf(item));
		}
		return original;
	}
}
