/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.torch;

import moriyashiine.enchancement.common.entity.projectile.TorchEntity;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {
	@ModifyVariable(method = "createArrowEntity", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"))
	private PersistentProjectileEntity enchancement$torch(PersistentProjectileEntity value, World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack) {
		if (projectileStack.isOf(Items.TORCH) || !(shooter instanceof PlayerEntity)) {
			int level = EnchantmentHelper.getLevel(ModEnchantments.TORCH, weaponStack);
			if (level > 0) {
				TorchEntity torch = new TorchEntity(world, shooter, projectileStack);
				torch.setDamage(torch.getDamage() / 5);
				torch.setIgnitionTime(level);
				return torch;
			}
		}
		return value;
	}
}
