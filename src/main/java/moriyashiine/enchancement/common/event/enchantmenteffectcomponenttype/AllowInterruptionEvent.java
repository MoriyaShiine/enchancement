/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.tag.ModDamageTypeTags;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class AllowInterruptionEvent implements ServerLivingEntityEvents.AfterDamage {
	@Override
	public void afterDamage(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) {
		if (source.getEntity() != null && !source.is(ModDamageTypeTags.DOES_NOT_INTERRUPT)) {
			ItemStack stack = entity.getUseItem();
			if (EnchantmentHelper.has(stack, ModEnchantmentEffectComponentTypes.ALLOW_INTERRUPTION)) {
				entity.releaseUsingItem();
				if (entity instanceof Player player) {
					player.getCooldowns().addCooldown(stack, 20);
				}
				ModEntityComponents.LIGHTNING_DASH.maybeGet(entity).ifPresent(lightningDashComponent -> {
					lightningDashComponent.cancel();
					lightningDashComponent.sync();
				});
			}
		}
	}
}
