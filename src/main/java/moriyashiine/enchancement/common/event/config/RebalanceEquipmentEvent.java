/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.config;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.tag.ModDamageTypeTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.util.enchantment.effect.MaceEffect;
import moriyashiine.strawberrylib.api.event.TickEntityEvent;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;

public class RebalanceEquipmentEvent {
	public static class Interrupt implements ServerLivingEntityEvents.AfterDamage {
		@Override
		public void afterDamage(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) {
			if (ModConfig.rebalanceEquipment && source.getEntity() != null && !source.is(ModDamageTypeTags.DOES_NOT_INTERRUPT) && entity instanceof Player player && isValid(player)) {
				player.getCooldowns().addCooldown(entity.getUseItem(), 20);
				entity.releaseUsingItem();
			}
		}
	}

	public static class Tick implements TickEntityEvent {
		@Override
		public void tick(Level level, Entity entity) {
			if (ModConfig.rebalanceEquipment && entity instanceof Player player && player.getTicksUsingItem() == EnchancementUtil.getTridentChargeTime() && isValid(player)) {
				SLibUtils.playSound(entity, ModSoundEvents.ENTITY_GENERIC_PING);
			}
		}
	}

	private static boolean isValid(Player player) {
		return player.getUseItem().getItem() instanceof TridentItem || MaceEffect.EFFECTS.stream().anyMatch(effect -> effect.isUsing(player));
	}
}
