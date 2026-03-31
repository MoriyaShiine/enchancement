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
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class BounceEvent {
	private static final List<Entry> ENTRIES = new ArrayList<>();

	private static void bounce(Entity entity, BounceComponent bounceComponent, double bounceStrength) {
		ModEntityComponents.AIR_MOBILITY.get(entity).enableResetBypass();
		bounceComponent.markBounced();
		entity.setDeltaMovement(entity.getDeltaMovement().x(), bounceStrength, entity.getDeltaMovement().z());
		entity.hurtMarked = true;
	}

	private static void scheduleBounce(Entity entity, double bounceStrength) {
		ENTRIES.add(new Entry(entity, bounceStrength));
	}

	public static class Bounce implements PreventFallDamageEvent {
		@Override
		public TriState preventsFallDamage(Level level, LivingEntity entity, double fallDistance, float damageModifier, DamageSource source) {
			if (!source.is(DamageTypes.STALAGMITE) && fallDistance > entity.getMaxFallDistance() && EnchancementUtil.hasAnyEnchantmentsWith(entity, ModEnchantmentEffectComponentTypes.BOUNCE)) {
				SLibUtils.playSound(entity, SoundEvents.SLIME_BLOCK_FALL);
				BounceComponent bounceComponent = ModEntityComponents.BOUNCE.get(entity);
				if (shouldBounce(entity, bounceComponent)) {
					double bounceStrength = EnchancementUtil.altLog(1.05, fallDistance / 7, 1 / 16F);
					if (entity.isAlwaysTicking() || entity.isClientAuthoritative()) {
						bounce(entity, bounceComponent, bounceStrength);
					} else {
						scheduleBounce(entity, bounceStrength);
					}
				}
				return TriState.TRUE;
			}
			return TriState.DEFAULT;
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
		public void onEndTick(MinecraftServer server) {
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
