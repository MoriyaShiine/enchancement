/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.internal;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class SyncVelocitiesEvent implements ServerTickEvents.EndTick {
	public static final Map<UUID, Vec3d> VELOCITIES = new HashMap<>();
	private static final Set<UUID> TO_REMOVE = new HashSet<>();

	@Override
	public void onEndTick(MinecraftServer server) {
		VELOCITIES.forEach((uuid, vec3d) -> {
			PlayerEntity player = server.getPlayerManager().getPlayer(uuid);
			if (player == null || !player.isAlive()) {
				TO_REMOVE.add(uuid);
			}
		});
		TO_REMOVE.forEach(VELOCITIES::remove);
		TO_REMOVE.clear();
	}
}
