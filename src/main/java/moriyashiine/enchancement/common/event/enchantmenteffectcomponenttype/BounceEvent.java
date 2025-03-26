/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.component.entity.BounceComponent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.PreventFallDamageEvent;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BounceEvent {
	private static final List<Entry> ENTRIES = new ArrayList<>();

	private static void bounce(Entity entity, BounceComponent bounceComponent, double bounceStrength) {
		ModEntityComponents.AIR_MOBILITY.get(entity).enableResetBypass();
		bounceComponent.markBounced();
		entity.setVelocity(entity.getVelocity().getX(), bounceStrength, entity.getVelocity().getZ());
		entity.velocityModified = true;
	}

	private static void scheduleBounce(Entity entity, double bounceStrength) {
		ENTRIES.add(new Entry(entity, bounceStrength));
	}

	public static class Bounce implements PreventFallDamageEvent {
		@Override
		public boolean shouldNotTakeFallDamage(World world, LivingEntity entity, double fallDistance, float damagePerDistance, DamageSource damageSource) {
			if (!damageSource.isOf(DamageTypes.STALAGMITE) && fallDistance > entity.getSafeFallDistance() && EnchancementUtil.hasAnyEnchantmentsWith(entity, ModEnchantmentEffectComponentTypes.BOUNCE)) {
				SLibUtils.playSound(entity, SoundEvents.BLOCK_SLIME_BLOCK_FALL);
				BounceComponent bounceComponent = ModEntityComponents.BOUNCE.get(entity);
				if (shouldBounce(entity, bounceComponent)) {
					double bounceStrength = Math.log((fallDistance / 7) + 1) / Math.log(1.05) / 16;
					if (entity.isPlayer() || entity.isControlledByPlayer()) {
						bounce(entity, bounceComponent, bounceStrength);
					} else {
						scheduleBounce(entity, bounceStrength);
					}
				}
				return true;
			}
			return false;
		}

		private static boolean shouldBounce(LivingEntity entity, BounceComponent bounceComponent) {
			boolean bounce = !SLibUtils.isCrouching(entity, true);
			if (bounceComponent.hasInvertedBounce()) {
				bounce = !bounce;
			}
			return bounce;
		}
	}

	public static class DelayedBounce implements ServerTickEvents.EndTick {
		@Override
		public void onEndTick(MinecraftServer minecraftServer) {
			for (int i = ENTRIES.size() - 1; i >= 0; i--) {
				Entry entry = ENTRIES.get(i);
				Entity entity = entry.entity();
				if (entity instanceof LivingEntity living && living.isAlive()) {
					bounce(entity, ModEntityComponents.BOUNCE.get(living), entry.bounceStrength());
				}
				ENTRIES.remove(i);
			}
		}
	}

	private record Entry(Entity entity, double bounceStrength) {
	}
}
