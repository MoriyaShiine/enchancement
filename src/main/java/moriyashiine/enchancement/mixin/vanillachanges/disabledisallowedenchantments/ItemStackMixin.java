package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Inject(method = "addEnchantment", at = @At("HEAD"), cancellable = true)
	private void enchancement$disableDisallowedEnchantments(Enchantment enchantment, int level, CallbackInfo ci) {
		if (Enchancement.getConfig().isEnchantmentDisallowed(enchantment)) {
			ci.cancel();
		}
	}
}
