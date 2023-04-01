/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.torch;

import moriyashiine.enchancement.common.entity.projectile.TorchEntity;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
	@Unique
	private static boolean torchSpeed = false;

	@Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setCurrentHand(Lnet/minecraft/util/Hand;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void enchancement$torch(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir, ItemStack crossbow) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.TORCH, crossbow) && user.getOffHandStack().isOf(Items.TORCH)) {
			torchSpeed = true;
		}
	}

	@ModifyVariable(method = "createArrow", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"))
	private static PersistentProjectileEntity enchancement$torch(PersistentProjectileEntity value, World world, LivingEntity entity, ItemStack crossbow, ItemStack arrow) {
		if (arrow.isOf(Items.TORCH)) {
			TorchEntity torch = new TorchEntity(world, entity);
			torch.setDamage(torch.getDamage() / 5);
			return torch;
		}
		return value;
	}

	@Inject(method = "getPullTime", at = @At("HEAD"), cancellable = true)
	private static void enchancement$torch(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (torchSpeed) {
			torchSpeed = false;
			cir.setReturnValue(2);
		}
	}
}
