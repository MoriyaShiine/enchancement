package moriyashiine.enchancement.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

public class FrostbiteEnchantment extends Enchantment {
	public FrostbiteEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
		super(weight, type, slotTypes);
	}

	@Override
	protected boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != Enchantments.FIRE_ASPECT;
	}

	@Override
	public void onTargetDamaged(LivingEntity user, Entity target, int level) {
		if (!user.world.isClient) {
			int frozenTicks = target.getFrozenTicks();
			if (frozenTicks < 600) {
				target.setFrozenTicks(Math.min(600, frozenTicks + 200));
			}
		}
	}
}
