/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.brimstone;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.Brimstone;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Player.class, Monster.class})
public class BrimstoneCrossbowMixin {
	@Inject(method = "getProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ProjectileWeaponItem;getSupportedHeldProjectiles()Ljava/util/function/Predicate;"), cancellable = true)
	private void enchancement$brimstone(ItemStack heldWeapon, CallbackInfoReturnable<ItemStack> cir) {
		if (EnchantmentHelper.has(heldWeapon, ModEnchantmentEffectComponentTypes.BRIMSTONE)) {
			cir.setReturnValue(Brimstone.BRIMSTONE_STACK.copy());
		}
	}
}
