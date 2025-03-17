/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.BuryEntityComponent;
import moriyashiine.enchancement.common.enchantment.effect.entity.BuryEffect;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BuryEntityEvent {
	public static class Unbury implements ServerLivingEntityEvents.AllowDamage {
		@Override
		public boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
			BuryEntityComponent buryEntityComponent = ModEntityComponents.BURY_ENTITY.get(entity);
			if (buryEntityComponent.getBuryPos() != null) {
				entity.setPosition(entity.getX(), entity.getY() + 0.5, entity.getZ());
				buryEntityComponent.unbury();
				return false;
			}
			return true;
		}
	}

	public static class Use implements UseEntityCallback {
		@Override
		public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
			ItemStack stack = player.getStackInHand(hand);
			if (!player.getItemCooldownManager().isCoolingDown(stack) && EnchantmentHelper.hasAnyEnchantmentsWith(stack, ModEnchantmentEffectComponentTypes.BURY_ENTITY) && BuryEffect.bury(world, entity, () -> {
				if (!world.isClient) {
					int cooldown = MathHelper.floor(EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.BURY_ENTITY, (ServerWorld) world, stack, 0) * 20);
					if (cooldown > 0) {
						player.getItemCooldownManager().set(stack, cooldown);
					}
					player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
					stack.damage(1, player, LivingEntity.getSlotForHand(hand));
				}
			})) {
				return ActionResult.SUCCESS;
			}
			return ActionResult.PASS;
		}
	}
}
