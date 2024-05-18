/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.torch;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
	@Inject(method = "getPullTime", at = @At("HEAD"), cancellable = true)
	private static void enchancement$torch(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.TORCH, stack)) {
			cir.setReturnValue(2);
		}
	}
}
