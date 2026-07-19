package moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.EnchancementConfig;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.BounceComponent;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import moriyashiine.enchancement.common.payload.SyncInvertedBounceStatusPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;

public class BounceClientEvent implements ClientTickEvents.EndLevelTick {
	public static void init() {
		ClientTickEvents.END_LEVEL_TICK.register(new BounceClientEvent());
	}

	@Override
	public void onEndTick(ClientLevel level) {
		Player player = Minecraft.getInstance().player;
		if (player != null && !player.isSpectator()) {
			BounceComponent bounce = EnchancementEntityComponents.BOUNCE.get(player);
			boolean current = bounce.hasInvertedBounce();
			if (current != EnchancementConfig.invertedBounce) {
				bounce.setInvertedBounce(EnchancementConfig.invertedBounce);
				SyncInvertedBounceStatusPayload.send(EnchancementConfig.invertedBounce);
			}
		}
	}
}
