package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantments(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (Enchancement.getConfig().isEnchantmentDisallowed(Enchantment.class.cast(this))) {
			cir.setReturnValue(false);
		}
	}
}
