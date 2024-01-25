/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class EmptyEnchantment extends Enchantment {
	private final int maxLevel;

	public EmptyEnchantment(int maxLevel, Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
		super(weight, type, slotTypes);
		this.maxLevel = maxLevel;
	}

	public EmptyEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
		this(1, weight, type, slotTypes);
	}

	@Override
	public int getMinPower(int level) {
		return 20;
	}

	@Override
	public int getMaxPower(int level) {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getMaxLevel() {
		return maxLevel;
	}
}
