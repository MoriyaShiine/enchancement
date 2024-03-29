/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;

public class ShovelEnchantment extends EmptyEnchantment {
	public ShovelEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
		super(weight, type, slotTypes);
	}

	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		return stack.getItem() instanceof ShovelItem;
	}
}
