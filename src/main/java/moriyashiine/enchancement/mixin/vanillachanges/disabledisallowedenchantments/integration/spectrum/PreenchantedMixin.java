/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments.integration.spectrum;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.dafuqs.spectrum.items.Preenchanted;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(value = Preenchanted.class, remap = false)
public interface PreenchantedMixin {
	@WrapOperation(method = "getDefaultEnchantedStack", at = @At(value = "INVOKE", target = "Lde/dafuqs/spectrum/items/Preenchanted;getDefaultEnchantments()Ljava/util/Map;"))
	private Map<Enchantment, Integer> enchancement$spectrum$disableDisallowedEnchantmentsGet(Preenchanted instance, Operation<Map<Enchantment, Integer>> original) {
		Map<Enchantment, Integer> enchantments = original.call(instance);
		Map<Enchantment, Integer> newMap = new LinkedHashMap<>();
		for (Enchantment enchantment : enchantments.keySet()) {
			if (EnchancementUtil.isEnchantmentAllowed(enchantment)) {
				newMap.put(enchantment, enchantments.get(enchantment));
			}
		}
		return newMap;
	}

	@WrapOperation(method = "onlyHasPreEnchantments", at = @At(value = "INVOKE", target = "Lde/dafuqs/spectrum/items/Preenchanted;getDefaultEnchantments()Ljava/util/Map;"))
	private Map<Enchantment, Integer> enchancement$spectrum$disableDisallowedEnchantmentsPre(Preenchanted instance, Operation<Map<Enchantment, Integer>> original) {
		Map<Enchantment, Integer> enchantments = original.call(instance);
		Map<Enchantment, Integer> newMap = new LinkedHashMap<>();
		for (Enchantment enchantment : enchantments.keySet()) {
			if (EnchancementUtil.isEnchantmentAllowed(enchantment)) {
				newMap.put(enchantment, enchantments.get(enchantment));
			}
		}
		return newMap;
	}
}
