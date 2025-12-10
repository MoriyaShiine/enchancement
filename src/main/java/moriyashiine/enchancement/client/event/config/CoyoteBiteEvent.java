/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event.config;

import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.ModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttackRangeComponent;
import net.minecraft.entity.Entity;

public class CoyoteBiteEvent implements ClientTickEvents.EndWorldTick {
	private static final MinecraftClient client = MinecraftClient.getInstance();

	public static Entity target = null;
	public static int ticks = 0;

	private static boolean canAttack() {
		AttackRangeComponent component = client.player.getMainHandStack().get(DataComponentTypes.ATTACK_RANGE);
		return component == null || component.isWithinRange(client.player, client.crosshairTarget.getPos());
	}

	@Override
	public void onEndTick(ClientWorld world) {
		if (!EnchancementClient.betterCombatLoaded && ModConfig.coyoteBiteTicks > 0 && client.targetedEntity != null && canAttack()) {
			target = client.targetedEntity;
			ticks = ModConfig.coyoteBiteTicks;
		}
		if (ticks > 0) {
			ticks--;
		}
		if (ticks == 0 || target == null || target.isRemoved() || !target.isAlive()) {
			target = null;
			ticks = 0;
		}
	}
}
