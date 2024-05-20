/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.util.toggleablepassive;

import moriyashiine.enchancement.common.init.ModDataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GrindstoneScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrindstoneScreenHandler.class)
public class GrindstoneScreenHandlerMixin {
	@Inject(method = "grind", at = @At("TAIL"))
	private void enchancement$toggleablePassive(ItemStack item, CallbackInfoReturnable<ItemStack> cir) {
		if (!item.hasEnchantments() && item.contains(ModDataComponentTypes.TOGGLEABLE_PASSIVE)) {
			item.remove(ModDataComponentTypes.TOGGLEABLE_PASSIVE);
		}
	}
}
