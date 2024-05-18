/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.enchantment;

import moriyashiine.enchancement.client.payload.ResetFrozenTicksPayload;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class FrostbiteEnchantment extends Enchantment {
	public FrostbiteEnchantment(Properties properties) {
		super(properties);
	}

	@Override
	protected boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != Enchantments.FIRE_ASPECT;
	}

	@Override
	public void onTargetDamaged(LivingEntity user, Entity target, int level) {
		if (!target.getWorld().isClient && target instanceof LivingEntity living) {
			if (!living.isDead()) {
				int frozenTicks = level * 120;
				if (target.getFrozenTicks() < frozenTicks) {
					target.setFrozenTicks(frozenTicks);
				}
			} else if (target instanceof ServerPlayerEntity player) {
				ResetFrozenTicksPayload.send(player);
			}
		}
	}
}
