/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.tridentshaveinnateloyalty;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "getLoyalty", at = @At("HEAD"), cancellable = true)
	private static void enchancement$tridentsHaveInnateLoyalty(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (ModConfig.tridentsHaveInnateLoyalty && stack.getItem() instanceof TridentItem && !stack.isIn(ModTags.Items.NO_LOYALTY)) {
			cir.setReturnValue(Enchantments.LOYALTY.getMaxLevel());
		}
	}
}
