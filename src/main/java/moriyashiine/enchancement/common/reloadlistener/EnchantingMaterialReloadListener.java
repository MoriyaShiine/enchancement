/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.reloadlistener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.event.internal.SyncEnchantingMaterialMapEvent;
import moriyashiine.enchancement.common.screenhandler.EnchantingTableScreenHandler;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.SimpleResourceReloader;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryOps;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class EnchantingMaterialReloadListener extends SimpleResourceReloader<Map<Item, EnchantingTableScreenHandler.EnchantingMaterial>> {
	public static final Identifier ID = Enchancement.id("enchanting_material");

	private static final Codec<Ingredient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Ingredient.CODEC.fieldOf("ingredient").forGetter(Function.identity())
	).apply(instance, Function.identity()));

	@Override
	protected Map<Item, EnchantingTableScreenHandler.EnchantingMaterial> prepare(Store store) {
		Map<Item, EnchantingTableScreenHandler.EnchantingMaterial> materials = new HashMap<>();
		RegistryOps<JsonElement> registryOps = store.getOrThrow(ResourceLoader.RELOADER_REGISTRY_LOOKUP_KEY).getOps(JsonOps.INSTANCE);
		store.getResourceManager().findAllResources("enchanting_material", path -> path.getNamespace().equals(Enchancement.MOD_ID) && path.getPath().endsWith(".json")).forEach((identifier, resources) -> {
			for (Resource resource : resources) {
				try (InputStream stream = resource.getInputStream()) {
					JsonObject object = JsonParser.parseReader(new JsonReader(new InputStreamReader(stream))).getAsJsonObject();
					Identifier itemId = Identifier.of(identifier.getPath().substring(identifier.getPath().indexOf("/") + 1, identifier.getPath().length() - 5).replace("/", ":"));
					Item item = Registries.ITEM.get(itemId);
					if (item == Registries.ITEM.get(Registries.ITEM.getDefaultId()) && !itemId.equals(Registries.ITEM.getDefaultId())) {
						continue;
					}
					DataResult<Ingredient> ingredient = CODEC.parse(registryOps, object);
					materials.put(item, new EnchantingTableScreenHandler.EnchantingMaterial(ingredient.getOrThrow()));
				} catch (Exception exception) {
					Enchancement.LOGGER.error("{} in file '{}'", exception.getLocalizedMessage(), identifier);
				}
			}
		});
		return materials;
	}

	@Override
	protected void apply(Map<Item, EnchantingTableScreenHandler.EnchantingMaterial> prepared, Store store) {
		EnchantingTableScreenHandler.ENCHANTING_MATERIAL_MAP.clear();
		EnchantingTableScreenHandler.ENCHANTING_MATERIAL_MAP.putAll(prepared);
		SyncEnchantingMaterialMapEvent.shouldSend = true;
	}
}
