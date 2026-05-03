/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.event.config;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;

public class RebalanceEquipmentClientEvent implements ClientTickEvents.EndLevelTick {
	public static ItemStack lastStack = ItemStack.EMPTY;

	@Override
	public void onEndTick(ClientLevel level) {
		lastStack = Minecraft.getInstance().player.getWeaponItem();
	}
}
