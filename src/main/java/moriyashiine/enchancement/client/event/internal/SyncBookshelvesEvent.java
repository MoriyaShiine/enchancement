/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event.internal;

import moriyashiine.enchancement.client.gui.screen.EnchantingTableScreen;
import moriyashiine.enchancement.common.screenhandlers.EnchantingTableScreenHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Set;

public class SyncBookshelvesEvent implements ClientTickEvents.EndWorldTick {
	public static Set<RegistryEntry<Enchantment>> CHISELED_ENCHANTMENTS = null;

	@Override
	public void onEndTick(ClientWorld world) {
		if (CHISELED_ENCHANTMENTS != null) {
			ClientPlayerEntity player = MinecraftClient.getInstance().player;
			if (player != null && player.currentScreenHandler instanceof EnchantingTableScreenHandler handler) {
				if (MinecraftClient.getInstance().currentScreen instanceof EnchantingTableScreen screen) {
					screen.receivedPacket = true;
				}
				handler.chiseledEnchantments.clear();
				handler.chiseledEnchantments.addAll(CHISELED_ENCHANTMENTS);
				CHISELED_ENCHANTMENTS = null;
			}
		}
	}
}
