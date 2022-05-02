/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.allowinfinityoncrossbows;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
	@ModifyVariable(method = "createArrow", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"))
	private static PersistentProjectileEntity enchancement$allowInfinityOnCrossbows(PersistentProjectileEntity value, World world, LivingEntity entity, ItemStack crossbow, ItemStack arrow) {
		if (arrow.isOf(Items.ARROW) && Enchancement.getConfig().allowInfinityOnCrossbows && EnchancementUtil.hasEnchantment(Enchantments.INFINITY, crossbow)) {
			value.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
		}
		return value;
	}

	@ModifyVariable(method = "loadProjectile", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
	private static boolean enchancement$allowInfinityOnCrossbows(boolean simulated, LivingEntity shooter, ItemStack crossbow, ItemStack projectile) {
		if (projectile.isOf(Items.ARROW) && Enchancement.getConfig().allowInfinityOnCrossbows && EnchancementUtil.hasEnchantment(Enchantments.INFINITY, crossbow)) {
			return true;
		}
		return simulated;
	}
}
