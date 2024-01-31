/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.torch;

import moriyashiine.enchancement.common.entity.projectile.TorchEntity;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
	@ModifyVariable(method = "createArrow", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"))
	private static PersistentProjectileEntity enchancement$torch(PersistentProjectileEntity value, World world, LivingEntity entity, ItemStack crossbow, ItemStack arrow) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.TORCH, crossbow) && (arrow.isOf(Items.TORCH) || !(entity instanceof PlayerEntity))) {
			TorchEntity torch = new TorchEntity(world, entity);
			torch.setDamage(torch.getDamage() / 5);
			return torch;
		}
		return value;
	}

	@Inject(method = "getPullTime", at = @At("HEAD"), cancellable = true)
	private static void enchancement$torch(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.TORCH, stack)) {
			cir.setReturnValue(2);
		}
	}
}
