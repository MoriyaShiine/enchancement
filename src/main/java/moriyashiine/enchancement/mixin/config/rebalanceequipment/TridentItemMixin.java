/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TridentItem.class)
public class TridentItemMixin {
	@ModifyExpressionValue(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;willBreakNextUse()Z"))
	private boolean enchancement$rebalanceEquipment(boolean original, @Local(ordinal = 1) int i) {
		if (i < EnchancementUtil.getTridentChargeTime()) {
			return true;
		}
		return original;
	}
}
