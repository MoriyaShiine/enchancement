/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.reloadlisteners;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.event.MineOreVeinsEvent;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.io.InputStream;
import java.io.InputStreamReader;

public class MineOreVeinsBaseBlockReloadListener implements SimpleSynchronousResourceReloadListener {
	private static final Identifier ID = Enchancement.id("mine_ore_veins_base_block");

	@Override
	public Identifier getFabricId() {
		return ID;
	}

	@Override
	public void reload(ResourceManager manager) {
		MineOreVeinsEvent.BASE_BLOCK_MAP.clear();
		manager.findAllResources("mine_ore_veins_base_block", path -> path.getNamespace().equals(Enchancement.MOD_ID) && path.getPath().endsWith(".json")).forEach((identifier, resources) -> {
			for (Resource resource : resources) {
				try (InputStream stream = resource.getInputStream()) {
					JsonObject object = JsonParser.parseReader(new JsonReader(new InputStreamReader(stream))).getAsJsonObject();
					Identifier blockId = Identifier.of(identifier.getPath().substring(identifier.getPath().indexOf("/") + 1, identifier.getPath().length() - 5).replace("/", ":"));
					Block block = Registries.BLOCK.get(blockId);
					if (block == Registries.BLOCK.get(Registries.BLOCK.getDefaultId()) && !blockId.equals(Registries.BLOCK.getDefaultId())) {
						continue;
					}
					Identifier baseBlockId = Identifier.of(JsonHelper.getString(object, "base"));
					Block baseBlock = Registries.BLOCK.get(baseBlockId);
					if (baseBlock == Registries.BLOCK.get(Registries.BLOCK.getDefaultId()) && !baseBlockId.equals(Registries.BLOCK.getDefaultId())) {
						Enchancement.LOGGER.error("Unknown block '{}' in file '{}'", baseBlockId, identifier);
						continue;
					}
					MineOreVeinsEvent.BASE_BLOCK_MAP.put(block, baseBlock);
				} catch (Exception exception) {
					Enchancement.LOGGER.error("{} in file '{}'", exception.getLocalizedMessage(), identifier);
				}
			}
		});
	}
}
