/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.reloadlisteners;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.event.ExtractingEvent;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.io.InputStream;
import java.io.InputStreamReader;

public class ExtractingBaseBlockReloadListener implements SimpleSynchronousResourceReloadListener {
	private static final Identifier ID = Enchancement.id("extracting_base_block");

	@Override
	public Identifier getFabricId() {
		return ID;
	}

	@Override
	public void reload(ResourceManager manager) {
		ExtractingEvent.BASE_BLOCK_MAP.clear();
		manager.findAllResources("extracting_base_block", path -> path.getNamespace().equals(Enchancement.MOD_ID) && path.getPath().endsWith(".json")).forEach((identifier, resources) -> {
			for (Resource resource : resources) {
				try (InputStream stream = resource.getInputStream()) {
					JsonObject object = JsonParser.parseReader(new JsonReader(new InputStreamReader(stream))).getAsJsonObject();
					Identifier blockId = new Identifier(identifier.getPath().substring(identifier.getPath().indexOf("/") + 1, identifier.getPath().length() - 5).replace("/", ":"));
					Block block = Registries.BLOCK.get(blockId);
					if (block == Registries.BLOCK.get(Registries.BLOCK.getDefaultId()) && !blockId.equals(Registries.BLOCK.getDefaultId())) {
						continue;
					}
					String base;
					try {
						base = JsonHelper.getString(object, "base");
					} catch (JsonParseException exception) {
						Enchancement.LOGGER.error(exception.getLocalizedMessage() + " in file '" + identifier + "'");
						continue;
					}
					Identifier baseBlockId = new Identifier(base);
					Block baseBlock = Registries.BLOCK.get(baseBlockId);
					if (baseBlock == Registries.BLOCK.get(Registries.BLOCK.getDefaultId()) && !baseBlockId.equals(Registries.BLOCK.getDefaultId())) {
						Enchancement.LOGGER.error("Unknown block '{}' in file '{}'", baseBlockId, identifier);
						continue;
					}
					ExtractingEvent.BASE_BLOCK_MAP.put(block, baseBlock);
				} catch (Exception ignored) {
				}
			}
		});
	}
}
