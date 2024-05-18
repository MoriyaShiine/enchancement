/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.singlelevelmode;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@WrapOperation(method = "forEachEnchantment(Lnet/minecraft/enchantment/EnchantmentHelper$Consumer;Lnet/minecraft/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper$Consumer;accept(Lnet/minecraft/enchantment/Enchantment;I)V"))
	private static void enchancement$singleLevelMode(EnchantmentHelper.Consumer instance, Enchantment enchantment, int i, Operation<Void> original, EnchantmentHelper.Consumer consumer, ItemStack stack) {
		if (ModConfig.singleLevelMode) {
			i = EnchancementUtil.getModifiedMaxLevel(stack, EnchancementUtil.ORIGINAL_MAX_LEVELS.getInt(enchantment));
		}
		original.call(instance, enchantment, i);
	}

	@Inject(method = "getLevel", at = @At("RETURN"), cancellable = true)
	private static void enchancement$singleLevelMode(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (cir.getReturnValueI() > 0 && ModConfig.singleLevelMode) {
			cir.setReturnValue(EnchancementUtil.getModifiedMaxLevel(stack, EnchancementUtil.ORIGINAL_MAX_LEVELS.getInt(enchantment)));
		}
	}
}
