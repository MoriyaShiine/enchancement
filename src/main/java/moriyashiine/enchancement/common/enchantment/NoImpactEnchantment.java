package moriyashiine.enchancement.common.enchantment;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class NoImpactEnchantment extends EmptyEnchantment {
	public NoImpactEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
		super(weight, type, slotTypes);
	}

	@Override
	protected boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != ModEnchantments.IMPACT;
	}
}
