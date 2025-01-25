/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TridentItem.class)
public class TridentItemMixin {
	@WrapOperation(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getTridentSpinAttackStrength(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)F"))
	private float enchancement$rebalanceEnchantments(ItemStack stack, LivingEntity user, Operation<Float> original) {
		float strength = original.call(stack, user);
		if (ModConfig.rebalanceEnchantments) {
			strength *= 2 / 3F;
		}
		return strength;
	}
}
