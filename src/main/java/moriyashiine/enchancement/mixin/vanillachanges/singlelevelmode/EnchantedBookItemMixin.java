/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.singlelevelmode;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookItemMixin {
	@ModifyVariable(method = "addEnchantment", at = @At("HEAD"), argsOnly = true)
	private static EnchantmentLevelEntry enchancement$singleLevelMode(EnchantmentLevelEntry value) {
		if (ModConfig.singleLevelMode) {
			return new EnchantmentLevelEntry(value.enchantment, 1);
		}
		return value;
	}
}
