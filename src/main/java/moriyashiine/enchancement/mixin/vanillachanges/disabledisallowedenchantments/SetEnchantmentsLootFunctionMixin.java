package moriyashiine.enchancement.mixin.vanillachanges.disabledisallowedenchantments;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import net.minecraft.loot.provider.number.LootNumberProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(SetEnchantmentsLootFunction.class)
public class SetEnchantmentsLootFunctionMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
	private static Map<Enchantment, LootNumberProvider> enchancement$disableDisallowedEnchantments(Map<Enchantment, LootNumberProvider> enchantments) {
		for (Enchantment enchantment : enchantments.keySet()) {
			if (Enchancement.getConfig().isEnchantmentDisallowed(enchantment)) {
				enchantments.remove(enchantment);
			}
		}
		return enchantments;
	}
}
