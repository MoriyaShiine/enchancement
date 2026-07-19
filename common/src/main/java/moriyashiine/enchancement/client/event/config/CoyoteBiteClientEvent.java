package moriyashiine.enchancement.client.event.config;

import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.EnchancementConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.component.AttackRange;

public class CoyoteBiteClientEvent implements ClientTickEvents.EndLevelTick {
	public static void init() {
		ClientTickEvents.END_LEVEL_TICK.register(new CoyoteBiteClientEvent());
	}

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
		if (!EnchancementClient.betterCombatLoaded && EnchancementConfig.coyoteBiteTicks > 0 && client.crosshairPickEntity != null && canAttack()) {
			target = client.crosshairPickEntity;
			ticks = EnchancementConfig.coyoteBiteTicks;
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
