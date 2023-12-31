/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.event;

import moriyashiine.enchancement.common.ModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

public class CoyoteBiteEvent implements ClientTickEvents.EndWorldTick {
	public static Entity target = null;
	public static int ticks = 0;

	@Override
	public void onEndTick(ClientWorld world) {
		if (ModConfig.coyoteBiteTicks > 0 && MinecraftClient.getInstance().targetedEntity != null) {
			target = MinecraftClient.getInstance().targetedEntity;
			ticks = ModConfig.coyoteBiteTicks;
		}
		if (ticks > 0) {
			System.out.println(ticks);
			ticks--;
		}
		if (ticks == 0 || target == null || target.isRemoved() || !target.isAlive()) {
			target = null;
		}
	}
}
