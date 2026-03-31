/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.disabledurability;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Armadillo.class)
public class ArmadilloMixin {
	@Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"))
	private void enchancement$disableDurability(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, @Local(name = "itemStack") ItemStack itemStack) {
		if (EnchancementUtil.isUnbreakable(itemStack)) {
			player.getCooldowns().addCooldown(itemStack, 20);
		}
	}

	@ModifyExpressionValue(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"))
	private boolean enchancement$disableDurability(boolean original, Player player, @Local(name = "itemStack") ItemStack itemStack) {
		if (EnchancementUtil.isUnbreakable(itemStack) && player.getCooldowns().isOnCooldown(itemStack)) {
			return false;
		}
		return original;
	}
}
