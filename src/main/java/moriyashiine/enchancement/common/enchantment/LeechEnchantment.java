/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.enchantment;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.ParticleTypes;

public class LeechEnchantment extends NoRiptideEnchantment {
	public LeechEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
		super(weight, type, slotTypes);
	}

	@Override
	public void onTargetDamaged(LivingEntity user, Entity target, int level) {
		if (user instanceof MobEntity) {
			applyEffect(user, target, 1);
		}
	}

	public static void applyEffect(LivingEntity attacker, Entity target, float cooldown) {
		if (cooldown >= Enchancement.getConfig().weaponEnchantmentCooldownRequirement) {
			if (target.world.isClient) {
				for (int i = 0; i < 6; i++) {
					target.world.addParticle(ParticleTypes.DAMAGE_INDICATOR, target.getParticleX(1), target.getBodyY(0.5), target.getParticleZ(1), 0, 0, 0);
				}
			} else {
				attacker.heal(2);
			}
		}
	}
}
