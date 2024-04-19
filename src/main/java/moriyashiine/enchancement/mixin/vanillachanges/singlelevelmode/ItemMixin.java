/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.singlelevelmode;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
	@Inject(method = "getRarity", at = @At("RETURN"), cancellable = true)
	private void enchancement$singleLevelMode(ItemStack stack, CallbackInfoReturnable<Rarity> cir) {
		if (ModConfig.singleLevelMode && stack.hasEnchantments() && !EnchancementUtil.hasWeakEnchantments(stack)) {
			cir.setReturnValue(Rarity.values()[Math.min(cir.getReturnValue().ordinal() + 1, Rarity.values().length - 1)]);
		}
	}
}
