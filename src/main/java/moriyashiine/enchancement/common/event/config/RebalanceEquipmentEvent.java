/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.config;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.tag.ModDamageTypeTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.util.enchantment.MaceEffect;
import moriyashiine.strawberrylib.api.event.TickEntityEvent;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.TridentItem;
import net.minecraft.server.world.ServerWorld;

public class RebalanceEquipmentEvent {
	public static class Interrupt implements ServerLivingEntityEvents.AfterDamage {
		@Override
		public void afterDamage(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) {
			if (ModConfig.rebalanceEquipment && source.getAttacker() != null && !source.isIn(ModDamageTypeTags.DOES_NOT_INTERRUPT) && entity instanceof PlayerEntity player && isValid(player)) {
				player.getItemCooldownManager().set(entity.getActiveItem(), 20);
				entity.stopUsingItem();
			}
		}
	}

	public static class Tick implements TickEntityEvent {
		@Override
		public void tick(ServerWorld world, Entity entity) {
			if (ModConfig.rebalanceEquipment && entity instanceof PlayerEntity player && player.getItemUseTime() == EnchancementUtil.getTridentChargeTime() && isValid(player)) {
				SLibUtils.playSound(entity, ModSoundEvents.ENTITY_GENERIC_PING);
			}
		}
	}

	private static boolean isValid(PlayerEntity player) {
		return player.getActiveItem().getItem() instanceof TridentItem || MaceEffect.EFFECTS.stream().anyMatch(effect -> effect.isUsing(player));
	}
}
