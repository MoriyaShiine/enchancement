/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event.internal;

import moriyashiine.enchancement.common.screenhandlers.EnchantingTableScreenHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.HashSet;
import java.util.Set;

public class SyncBookshelvesEvent implements ClientTickEvents.EndWorldTick {
	public static final Set<RegistryEntry<Enchantment>> CHISELED_ENCHANTMENTS = new HashSet<>();

	@Override
	public void onEndTick(ClientWorld world) {
		if (!CHISELED_ENCHANTMENTS.isEmpty() && MinecraftClient.getInstance().player.currentScreenHandler instanceof EnchantingTableScreenHandler handler) {
			handler.chiseledEnchantments.clear();
			handler.chiseledEnchantments.addAll(CHISELED_ENCHANTMENTS);
			CHISELED_ENCHANTMENTS.clear();
		}
	}
}
