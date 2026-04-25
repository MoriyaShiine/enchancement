/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TridentItem.class)
public class TridentItemMixin {
	@ModifyExpressionValue(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;nextDamageWillBreak()Z"))
	private boolean enchancement$rebalanceEquipment(boolean original, @Local(argsOnly = true) ItemStack itemStack, @Local(name = "timeHeld") int timeHeld) {
		if (timeHeld < EnchancementUtil.getTridentChargeTime(itemStack)) {
			return true;
		}
		return original;
	}
}
