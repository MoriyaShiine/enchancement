/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.allowinfinityoncrossbows;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@SuppressWarnings("ConstantConditions")
	@Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
	private void enchancement$allowInfinityOnCrossbows(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (ModConfig.allowInfinityOnCrossbows && (Object) this == Enchantments.INFINITY && stack.getItem() instanceof CrossbowItem) {
			cir.setReturnValue(true);
		}
	}
}
