/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;

public class BouncyEnchantment extends Enchantment {
	public BouncyEnchantment(Properties properties) {
		super(properties);
	}

	@Override
	protected boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != Enchantments.FEATHER_FALLING;
	}
}
