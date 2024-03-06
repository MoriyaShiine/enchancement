/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.unbreakingchanges;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GrindstoneScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

// todo remove after 24w10a
@Mixin(GrindstoneScreenHandler.class)
public class GrindstoneScreenHandlerMixin {
	@Shadow
	@Final
	Inventory input;

	@ModifyExpressionValue(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;areEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"))
	private boolean enchancement$unbreakingChanges(boolean value) {
		if (value && (EnchancementUtil.shouldBeUnbreakable(input.getStack(0)) || EnchancementUtil.shouldBeUnbreakable(input.getStack(1)))) {
			return false;
		}
		return value;
	}
}
