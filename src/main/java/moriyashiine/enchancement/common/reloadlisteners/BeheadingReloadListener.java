/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.reloadlisteners;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.util.BeheadingEntry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class BeheadingReloadListener extends JsonDataLoader implements IdentifiableResourceReloadListener {
	private static final Identifier ID = new Identifier(Enchancement.MOD_ID, "beheading");

	public BeheadingReloadListener(Gson gson, String dataType) {
		super(gson, dataType);
	}

	@Override
	public Identifier getFabricId() {
		return ID;
	}

	@Override
	protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		BeheadingEntry.DROP_MAP.clear();
		prepared.forEach((identifier, jsonElement) -> {
			JsonObject object = JsonHelper.asObject(jsonElement, identifier.toString());
			EntityType<?> entity_type = Registry.ENTITY_TYPE.get(identifier);
			Item drop = Registry.ITEM.get(new Identifier(JsonHelper.getString(object, "drop")));
			float chance = JsonHelper.getFloat(object, "chance");
			BeheadingEntry.DROP_MAP.put(entity_type, new BeheadingEntry(drop, chance));
		});
	}
}
