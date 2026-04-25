/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.BuryEntityComponent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.world.item.effects.entity.BuryEffect;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jspecify.annotations.Nullable;

public class BuryEntityEvent {
	public static class Unbury implements ServerLivingEntityEvents.AllowDamage {
		@Override
		public boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
			BuryEntityComponent buryEntityComponent = ModEntityComponents.BURY_ENTITY.get(entity);
			if (buryEntityComponent.getBuryPos() != null) {
				entity.setPos(entity.getX(), entity.getY() + 0.5, entity.getZ());
				buryEntityComponent.unbury();
				return false;
			}
			return true;
		}
	}

	public static class Use implements UseEntityCallback {
		@Override
		public InteractionResult interact(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
			if (!player.isSpectator()) {
				ItemStack stack = player.getItemInHand(hand);
				if (!player.getCooldowns().isOnCooldown(stack) && EnchantmentHelper.has(stack, ModEnchantmentEffectComponentTypes.BURY_ENTITY) && BuryEffect.bury(level, entity, () -> {
					if (!level.isClientSide()) {
						int cooldown = Mth.floor(EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.BURY_ENTITY, (ServerLevel) level, stack, 0) * 20);
						if (cooldown > 0) {
							player.getCooldowns().addCooldown(stack, cooldown);
						}
						player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
						stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
					}
				})) {
					return InteractionResult.SUCCESS;
				}
			}
			return InteractionResult.PASS;
		}
	}
}
