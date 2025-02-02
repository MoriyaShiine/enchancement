/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.component.entity.BounceComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class BounceEvent implements ServerTickEvents.EndTick {
	private static final List<Entry> ENTRIES = new ArrayList<>();

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

	public static void bounce(Entity entity, BounceComponent bounceComponent, double bounceStrength) {
		ModEntityComponents.AIR_MOBILITY.get(entity).enableResetBypass();
		bounceComponent.markBounced();
		entity.setVelocity(entity.getVelocity().getX(), bounceStrength, entity.getVelocity().getZ());
		entity.velocityModified = true;
	}

	public static void scheduleBounce(Entity entity, double bounceStrength) {
		ENTRIES.add(new Entry(entity, bounceStrength));
	}

	public record Entry(Entity entity, double bounceStrength) {
	}
}
