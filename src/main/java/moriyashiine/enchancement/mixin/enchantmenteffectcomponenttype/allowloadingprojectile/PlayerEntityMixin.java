/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.allowloadingprojectile;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.enchantment.effect.AllowLoadingProjectileEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;
import java.util.function.Predicate;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@WrapOperation(method = "getProjectileType", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/RangedWeaponItem;getHeldProjectile(Lnet/minecraft/entity/LivingEntity;Ljava/util/function/Predicate;)Lnet/minecraft/item/ItemStack;"))
	private ItemStack enchancement$allowLoadingProjectileHeld(LivingEntity entity, Predicate<ItemStack> predicate, Operation<ItemStack> original, ItemStack weaponStack) {
		ItemStack projectileStack = original.call(entity, predicate);
		if (AllowLoadingProjectileEffect.disable(weaponStack, projectileStack)) {
			return ItemStack.EMPTY;
		}
		return projectileStack;
	}

	@ModifyExpressionValue(method = "getProjectileType", at = @At(value = "INVOKE", target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z"))
	private boolean enchancement$allowLoadingProjectile(boolean original, ItemStack weaponStack, @Local(ordinal = 1) ItemStack projectileStack) {
		if (AllowLoadingProjectileEffect.disable(weaponStack, projectileStack)) {
			return false;
		}
		return original;
	}

	@ModifyReturnValue(method = "getProjectileType", at = @At(value = "RETURN", ordinal = 3))
	private ItemStack enchancement$allowLoadingProjectile(ItemStack original, ItemStack weaponStack) {
		if (!original.isEmpty()) {
			Set<Item> items = AllowLoadingProjectileEffect.getItems(weaponStack);
			for (Item item : items) {
				return item.getDefaultStack();
			}
		}
		return original;
	}
}
