/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.enchantment;

import de.dafuqs.spectrum.items.tools.WorkstaffItem;
import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class MoltenEnchantment extends NoSilkTouchEnchantment {
	public MoltenEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
		super(weight, type, slotTypes);
	}

	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		if (Enchancement.isSpectrumLoaded && stack.getItem() instanceof WorkstaffItem) {
			return true;
		}
		return super.isAcceptableItem(stack);
	}
}
