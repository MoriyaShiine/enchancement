package moriyashiine.enchancement.common.util;

import com.google.gson.JsonObject;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class BeheadingEntry {
	public static final Map<EntityType<?>, BeheadingEntry> DROP_MAP = new HashMap<>();

	public final Item drop;
	public final float chance;

	public BeheadingEntry(Item drop, float chance) {
		this.drop = drop;
		this.chance = chance;
	}

	public static void initEvent() {
		LootTableLoadingCallback.EVENT.register((resourceManager, manager, id, supplier, setter) -> {
			if (id.getPath().startsWith(Enchancement.MOD_ID + "_beheading")) {
				try {
					for (Resource resource : resourceManager.getAllResources(new Identifier(id.getNamespace(), "loot_tables/" + id.getPath() + ".json"))) {
						JsonObject object = JsonHelper.deserialize(new InputStreamReader(resource.getInputStream()));
						EntityType<?> entity_type = Registry.ENTITY_TYPE.get(new Identifier(JsonHelper.getString(object, "entity_type")));
						Item drop = Registry.ITEM.get(new Identifier(JsonHelper.getString(object, "drop")));
						float chance = JsonHelper.getFloat(object, "chance");
						DROP_MAP.put(entity_type, new BeheadingEntry(drop, chance));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
