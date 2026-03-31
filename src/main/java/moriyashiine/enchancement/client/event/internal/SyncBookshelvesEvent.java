/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.event.internal;

import moriyashiine.enchancement.client.gui.screens.inventory.ModEnchantmentScreen;
import moriyashiine.enchancement.common.world.inventory.ModEnchantmentMenu;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Set;

public class SyncBookshelvesEvent implements ClientTickEvents.EndLevelTick {
	public static Set<Holder<Enchantment>> CHISELED_ENCHANTMENTS = null;

	@Override
	public void onEndTick(ClientLevel level) {
		if (CHISELED_ENCHANTMENTS != null) {
			Minecraft client = Minecraft.getInstance();
			Player player = client.player;
			if (player != null && player.containerMenu instanceof ModEnchantmentMenu menu) {
				if (client.screen instanceof ModEnchantmentScreen screen) {
					screen.receivedPacket = true;
				}
				menu.chiseledEnchantments.clear();
				menu.chiseledEnchantments.addAll(CHISELED_ENCHANTMENTS);
				CHISELED_ENCHANTMENTS = null;
			}
		}
	}
}
