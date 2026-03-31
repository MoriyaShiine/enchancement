/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.internal;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class SyncDeltaMovementsEvent implements ServerTickEvents.EndTick {
	public static final Map<UUID, Vec3> DELTAS = new HashMap<>();
	private static final Set<UUID> TO_REMOVE = new HashSet<>();

	@Override
	public void onEndTick(MinecraftServer server) {
		DELTAS.forEach((uuid, _) -> {
			Player player = server.getPlayerList().getPlayer(uuid);
			if (player == null || !player.isAlive()) {
				TO_REMOVE.add(uuid);
			}
		});
		TO_REMOVE.forEach(DELTAS::remove);
		TO_REMOVE.clear();
	}
}
