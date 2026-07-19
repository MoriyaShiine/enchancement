package moriyashiine.enchancement.common.event.internal;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SyncDeltaMovementsEvent implements ServerTickEvents.EndTick {
	public static void init() {
		ServerTickEvents.END_SERVER_TICK.register(new SyncDeltaMovementsEvent());
	}

	public static final Map<UUID, Vec3> DELTAS = new HashMap<>();

	@Override
	public void onEndTick(MinecraftServer server) {
		DELTAS.keySet().removeIf(uuid -> {
			Player player = server.getPlayerList().getPlayer(uuid);
			return player == null || !player.slib$exists();
		});
	}
}
