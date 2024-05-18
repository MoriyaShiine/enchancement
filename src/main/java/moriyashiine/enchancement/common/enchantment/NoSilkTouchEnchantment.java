/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;

public class NoSilkTouchEnchantment extends Enchantment {
	public NoSilkTouchEnchantment(Properties properties) {
		super(properties);
	}

	@Override
	protected boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != Enchantments.SILK_TOUCH;
	}
}
