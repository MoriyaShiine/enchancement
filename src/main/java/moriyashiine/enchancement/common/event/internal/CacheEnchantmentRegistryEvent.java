/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event.internal;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;

public class CacheEnchantmentRegistryEvent implements ServerLifecycleEvents.ServerStarted {
	@Override
	public void onServerStarted(MinecraftServer server) {
		EnchancementUtil.ENCHANTMENTS.clear();
		EnchancementUtil.ENCHANTMENTS.addAll(server.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).streamEntries().toList());
		EnchancementUtil.SERVER_RANDOM = server.getOverworld().getRandom();
	}
}
