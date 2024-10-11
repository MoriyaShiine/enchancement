/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class AllowInterruptionEvent implements ServerLivingEntityEvents.AllowDamage {
	@Override
	public boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
		if (source.getAttacker() != null) {
			ItemStack stack = entity.getActiveItem();
			if (EnchantmentHelper.hasAnyEnchantmentsWith(stack, ModEnchantmentEffectComponentTypes.ALLOW_INTERRUPTION)) {
				entity.stopUsingItem();
				if (entity instanceof PlayerEntity player) {
					player.getItemCooldownManager().set(stack.getItem(), 20);
				}
			}
		}
		return true;
	}
}
