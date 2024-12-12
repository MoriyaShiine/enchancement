/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.common.payload.SyncBounceInvertedStatusPayload;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

public class BounceEvent implements ServerTickEvents.EndTick {
	@Override
	public void onEndTick(MinecraftServer server) {
		SyncBounceInvertedStatusPayload.ENTITIES_WITH_INVERTED_STATUS.removeIf(entity -> entity == null || entity.isRemoved());
	}
}
