/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.enchantment;

import net.minecraft.item.ItemStack;

public class MoltenEnchantment extends NoSilkTouchEnchantment {
	public MoltenEnchantment(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		// todo spectrum
//		if (Enchancement.isSpectrumLoaded && stack.getItem() instanceof WorkstaffItem) {
//			return true;
//		}
		return super.isAcceptableItem(stack);
	}
}
