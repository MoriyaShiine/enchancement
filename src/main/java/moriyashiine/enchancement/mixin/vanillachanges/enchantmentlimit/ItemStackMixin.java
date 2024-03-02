/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract NbtList getEnchantments();

	@Inject(method = "addEnchantment", at = @At("HEAD"), cancellable = true)
	private void enchancement$enchantmentLimit(Enchantment enchantment, int level, CallbackInfo ci) {
		if (EnchancementUtil.limitCheck(false, EnchancementUtil.getNonDefaultEnchantmentsSize((ItemStack) (Object) this, getEnchantments().size()) >= ModConfig.enchantmentLimit)) {
			ci.cancel();
		}
	}
}
