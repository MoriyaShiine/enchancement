package moriyashiine.enchancement.mixin.vanillachanges.unbreakingchanges;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Inject(method = "isDamageable", at = @At("HEAD"), cancellable = true)
	private void enchancement$unbreakingChanges(CallbackInfoReturnable<Boolean> cir) {
		if (EnchancementUtil.shouldBeUnbreakable(ItemStack.class.cast(this))) {
			cir.setReturnValue(false);
		}
	}
}
