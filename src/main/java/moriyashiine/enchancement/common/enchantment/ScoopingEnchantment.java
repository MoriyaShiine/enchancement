/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

public class ScoopingEnchantment extends Enchantment {
	public ScoopingEnchantment(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		// todo spectrum
//		if (Enchancement.isSpectrumLoaded && stack.isOf(SpectrumItems.RAZOR_FALCHION)) {
//			return true;
//		}
		return super.isAcceptableItem(stack);
	}
}
