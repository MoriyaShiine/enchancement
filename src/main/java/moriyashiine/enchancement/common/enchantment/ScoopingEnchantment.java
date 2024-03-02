/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.enchantment;

import de.dafuqs.spectrum.registries.SpectrumItems;
import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class ScoopingEnchantment extends ShovelEnchantment {
	public ScoopingEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
		super(weight, type, slotTypes);
	}

	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		if (Enchancement.isSpectrumLoaded && stack.isOf(SpectrumItems.RAZOR_FALCHION)) {
			return true;
		}
		return super.isAcceptableItem(stack);
	}
}
