/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.brimstone;

import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerEntity.class, HostileEntity.class})
public class BrimstoneCrossbowMixin {
	@Inject(method = "getProjectileType", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/RangedWeaponItem;getHeldProjectiles()Ljava/util/function/Predicate;"), cancellable = true)
	private void enchancement$brimstone(ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.BRIMSTONE, stack)) {
			cir.setReturnValue(BrimstoneEntity.BRIMSTONE_STACK.copy());
		}
	}
}
