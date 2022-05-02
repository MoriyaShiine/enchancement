package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "getPossibleEntries", at = @At("RETURN"))
	private static void enchancement$disableDisallowedEnchantments(int power, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
		if (!Enchancement.getConfig().allowedEnchantmentIdentifiers.isEmpty()) {
			List<EnchantmentLevelEntry> entries = cir.getReturnValue();
			for (int i = entries.size() - 1; i >= 0; i--) {
				if (Enchancement.getConfig().isEnchantmentDisallowed(entries.get(i).enchantment)) {
					entries.remove(i);
				}
			}
		}
	}

	@ModifyVariable(method = "set", at = @At("HEAD"), argsOnly = true)
	private static Map<Enchantment, Integer> enchancement$disableDisallowedEnchantments(Map<Enchantment, Integer> value) {
		if (!Enchancement.getConfig().allowedEnchantmentIdentifiers.isEmpty()) {
			for (Enchantment enchantment : value.keySet()) {
				if (Enchancement.getConfig().isEnchantmentDisallowed(enchantment)) {
					value.remove(enchantment);
				}
			}
		}
		return value;
	}
}
