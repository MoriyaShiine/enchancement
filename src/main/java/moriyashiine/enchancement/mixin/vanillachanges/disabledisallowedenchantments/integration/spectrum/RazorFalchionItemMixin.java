/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments.integration.spectrum;

import de.dafuqs.spectrum.items.tools.RazorFalchionItem;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(value = RazorFalchionItem.class, remap = false)
public class RazorFalchionItemMixin {
	@Inject(method = "getDefaultEnchantments", at = @At("HEAD"), cancellable = true)
	private void enchancement$spectrum$replaceDisallowedEnchantments(CallbackInfoReturnable<Map<Enchantment, Integer>> cir) {
		if (!EnchancementUtil.isEnchantmentAllowed(Enchantments.LOOTING)) {
			cir.setReturnValue(Map.of(ModEnchantments.SCOOPING, ModEnchantments.SCOOPING.getMaxLevel()));
		}
	}
}
