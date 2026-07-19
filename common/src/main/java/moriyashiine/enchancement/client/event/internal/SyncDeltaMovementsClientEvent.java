package moriyashiine.enchancement.client.event.internal;

import moriyashiine.enchancement.common.payload.SyncDeltaMovementPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;

public class SyncDeltaMovementsClientEvent implements ClientTickEvents.StartLevelTick {
	public static void init() {
		ClientTickEvents.START_LEVEL_TICK.register(new SyncDeltaMovementsClientEvent());
	}

	@Override
	public void onStartTick(ClientLevel level) {
		Player player = Minecraft.getInstance().player;
		if (player != null && player.slib$exists()) {
			SyncDeltaMovementPayload.send(player.getDeltaMovement());
		}
	}
}
