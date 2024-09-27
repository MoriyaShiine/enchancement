/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.resource.LifecycledResourceManager;
import net.minecraft.server.MinecraftServer;

public class InitializeDefaultEnchantmentsEvent {
	public static final Object2ObjectMap<Item, ItemEnchantmentsComponent> DEFAULT_ENCHANTMENTS = new Object2ObjectOpenHashMap<>();

	public static class ServerStart implements ServerLifecycleEvents.ServerStarted {
		@Override
		public void onServerStarted(MinecraftServer server) {
			populate(server);
		}
	}

	public static class ReloadResources implements ServerLifecycleEvents.EndDataPackReload {
		@Override
		public void endDataPackReload(MinecraftServer server, LifecycledResourceManager resourceManager, boolean success) {
			if (success) {
				populate(server);
			}
		}
	}

	private static void populate(MinecraftServer server) {
		DEFAULT_ENCHANTMENTS.clear();
		for (RecipeEntry<?> recipe : server.getRecipeManager().sortedValues()) {
			ItemStack stack = recipe.value().getResult(server.getRegistryManager());
			ItemEnchantmentsComponent enchantments = EnchantmentHelper.getEnchantments(stack);
			if (!enchantments.isEmpty()) {
				DEFAULT_ENCHANTMENTS.put(stack.getItem(), enchantments);
			}
		}
	}
}
