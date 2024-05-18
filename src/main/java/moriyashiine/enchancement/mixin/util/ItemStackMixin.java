/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.util;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract ItemEnchantmentsComponent getEnchantments();

	@ModifyExpressionValue(method = "isEnchantable", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/ItemEnchantmentsComponent;isEmpty()Z"))
	private boolean enchancement$dontCountDefaultEnchantments(boolean value) {
		return value && EnchancementUtil.limitCheck(true, EnchancementUtil.getNonDefaultEnchantmentsSize((ItemStack) (Object) this, getEnchantments().getSize()) == 0);
	}
}
