/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.reloadlisteners;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.screenhandlers.EnchantingTableScreenHandler;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.io.InputStream;
import java.io.InputStreamReader;

public class EnchantingMaterialReloadListener implements SimpleSynchronousResourceReloadListener {
	private static final Identifier ID = Enchancement.id("enchanting_material");

	@Override
	public Identifier getFabricId() {
		return ID;
	}

	@Override
	public void reload(ResourceManager manager) {
		EnchantingTableScreenHandler.ENCHANTING_MATERIAL_MAP.clear();
		manager.findAllResources("enchanting_material", path -> path.getNamespace().equals(Enchancement.MOD_ID) && path.getPath().endsWith(".json")).forEach((identifier, resources) -> {
			for (Resource resource : resources) {
				try (InputStream stream = resource.getInputStream()) {
					JsonObject object = JsonParser.parseReader(new JsonReader(new InputStreamReader(stream))).getAsJsonObject();
					Identifier itemId = new Identifier(identifier.getPath().substring(identifier.getPath().indexOf("/") + 1, identifier.getPath().length() - 5).replace("/", ":"));
					Item item = Registries.ITEM.get(itemId);
					Ingredient ingredient;
					try {
						ingredient = Ingredient.fromJson(JsonHelper.getObject(object, "ingredient"));
					} catch (JsonParseException exception) {
						Enchancement.LOGGER.error(exception.getLocalizedMessage() + " in file '" + identifier + "'");
						continue;
					}
					EnchantingTableScreenHandler.ENCHANTING_MATERIAL_MAP.put(item, ingredient);
				} catch (Exception ignored) {
				}
			}
		});
	}
}
