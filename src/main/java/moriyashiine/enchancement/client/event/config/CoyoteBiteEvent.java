/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.event.config;

import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.ModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.component.AttackRange;

public class CoyoteBiteEvent implements ClientTickEvents.EndLevelTick {
	private static final Minecraft client = Minecraft.getInstance();

	public static Entity target = null;
	public static int ticks = 0;

	private static boolean canAttack() {
		if (client.player.getMainHandItem().has(DataComponents.PIERCING_WEAPON)) {
			return false;
		}
		AttackRange component = client.player.getMainHandItem().get(DataComponents.ATTACK_RANGE);
		return component == null || component.isInRange(client.player, client.hitResult.getLocation());
	}

	@Override
	public void onEndTick(ClientLevel level) {
		if (!EnchancementClient.betterCombatLoaded && ModConfig.coyoteBiteTicks > 0 && client.crosshairPickEntity != null && canAttack()) {
			target = client.crosshairPickEntity;
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
