/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.alltridentshaveloyalty;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.registry.ModTags;
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
	private static void enchancement$allTridentsHaveLoyalty(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (Enchancement.getConfig().allTridentsHaveLoyalty && stack.getItem() instanceof TridentItem && !stack.isIn(ModTags.Items.NO_LOYALTY)) {
			cir.setReturnValue(Enchantments.LOYALTY.getMaxLevel());
		}
	}
}
