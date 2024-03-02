/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.event;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.resource.LifecycledResourceManager;
import net.minecraft.server.MinecraftServer;

import java.util.Map;

public class InitializeDefaultEnchantmentsEvent {
	public static final Object2ObjectMap<Item, Map<Enchantment, Integer>> DEFAULT_ENCHANTMENTS = new Object2ObjectOpenHashMap<>();

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

	private static <C extends Inventory, T extends Recipe<C>> void populate(MinecraftServer server) {
		DEFAULT_ENCHANTMENTS.clear();
		for (RecipeType<?> type : Registries.RECIPE_TYPE) {
			try {
				for (Recipe<?> recipe : server.getRecipeManager().listAllOfType((RecipeType<T>) type)) {
					ItemStack stack = recipe.getOutput(server.getRegistryManager());
					Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
					if (!enchantments.isEmpty()) {
						DEFAULT_ENCHANTMENTS.put(stack.getItem(), enchantments);
					}
				}
			} catch (Exception ignore) {
			}
		}
	}
}
