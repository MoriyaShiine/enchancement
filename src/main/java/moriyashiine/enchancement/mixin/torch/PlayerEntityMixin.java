/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.torch;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Predicate;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@ModifyVariable(method = "getProjectileType", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/RangedWeaponItem;getProjectiles()Ljava/util/function/Predicate;"))
	private Predicate<ItemStack> enchancement$torch(Predicate<ItemStack> value, ItemStack stack) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.TORCH, stack)) {
			return projectile -> false;
		}
		return value;
	}

	@ModifyReturnValue(method = "getProjectileType", at = @At(value = "RETURN", ordinal = 3))
	private ItemStack enchancement$torch(ItemStack original, ItemStack weaponStack) {
		if (!original.isEmpty() && EnchancementUtil.hasEnchantment(ModEnchantments.TORCH, weaponStack)) {
			return Items.TORCH.getDefaultStack();
		}
		return original;
	}
}
