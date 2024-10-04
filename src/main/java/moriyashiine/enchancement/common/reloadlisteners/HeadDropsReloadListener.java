/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.reloadlisteners;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.util.BeheadingEntry;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.io.InputStream;
import java.io.InputStreamReader;

public class HeadDropsReloadListener implements SimpleSynchronousResourceReloadListener {
	private static final Identifier ID = Enchancement.id("head_drops");

	@Override
	public Identifier getFabricId() {
		return ID;
	}

	@Override
	public void reload(ResourceManager manager) {
		BeheadingEntry.DROP_MAP.clear();
		manager.findAllResources("head_drops", path -> path.getNamespace().equals(Enchancement.MOD_ID) && path.getPath().endsWith(".json")).forEach((identifier, resources) -> {
			for (Resource resource : resources) {
				try (InputStream stream = resource.getInputStream()) {
					JsonObject object = JsonParser.parseReader(new JsonReader(new InputStreamReader(stream))).getAsJsonObject();
					Identifier entityId = Identifier.of(identifier.getPath().substring(identifier.getPath().indexOf("/") + 1, identifier.getPath().length() - 5).replace("/", ":"));
					EntityType<?> entity_type = Registries.ENTITY_TYPE.get(entityId);
					if (entity_type == Registries.ENTITY_TYPE.get(Registries.ENTITY_TYPE.getDefaultId()) && !entityId.equals(Registries.ENTITY_TYPE.getDefaultId())) {
						continue;
					}
					Identifier dropId = Identifier.of(JsonHelper.getString(object, "drop"));
					Item drop = Registries.ITEM.get(dropId);
					if (drop == Registries.ITEM.get(Registries.ITEM.getDefaultId()) && !dropId.equals(Registries.ITEM.getDefaultId())) {
						Enchancement.LOGGER.error("Unknown item '{}' in file '{}'", dropId, identifier);
						continue;
					}
					float chance = JsonHelper.getFloat(object, "chance");
					BeheadingEntry.DROP_MAP.put(entity_type, new BeheadingEntry(drop, chance));
				} catch (Exception ignored) {
				}
			}
		});
	}
}
