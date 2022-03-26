package moriyashiine.enchancement.mixin.vanillachanges;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Inject(method = "isDamageable", at = @At("HEAD"), cancellable = true)
	private void enchancement$unbreakableUnbreaking(CallbackInfoReturnable<Boolean> cir) {
		int level = Enchancement.getConfig().unbreakableUnbreakingLevel;
		if (level > -1 && EnchantmentHelper.getLevel(Enchantments.UNBREAKING, ItemStack.class.cast(this)) > level) {
			cir.setReturnValue(false);
		}
	}
}
