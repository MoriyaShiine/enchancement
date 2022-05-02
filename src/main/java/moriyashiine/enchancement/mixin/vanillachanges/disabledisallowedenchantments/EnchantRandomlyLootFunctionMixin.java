package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(EnchantRandomlyLootFunction.class)
public class EnchantRandomlyLootFunctionMixin {
	@Inject(method = "addEnchantmentToStack", at = @At("HEAD"), cancellable = true)
	private static void enchancement$disableDisallowedEnchantments(ItemStack stack, Enchantment enchantment, Random random, CallbackInfoReturnable<ItemStack> cir) {
		if (Enchancement.getConfig().isEnchantmentDisallowed(enchantment)) {
			cir.setReturnValue(stack);
		}
	}
}
