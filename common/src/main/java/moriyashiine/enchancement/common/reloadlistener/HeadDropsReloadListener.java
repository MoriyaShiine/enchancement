package moriyashiine.enchancement.common.reloadlistener;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.util.enchantment.HeadDrop;
import net.fabricmc.fabric.api.resource.v1.reloader.SimpleReloadListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.scanDirectory;

public class HeadDropsReloadListener extends SimpleReloadListener<Map<EntityType<?>, HeadDrop>> {
	public static final String DIRECTORY = Enchancement.MOD_ID + "/head_drops";

	@Override
	protected Map<EntityType<?>, HeadDrop> prepare(SharedState sharedState) {
		Map<EntityType<?>, HeadDrop> map = new HashMap<>();
		scanDirectory(sharedState, DIRECTORY, HeadDrop.CODEC).forEach((identifier, entry) -> BuiltInRegistries.ENTITY_TYPE.getOptional(identifier).ifPresent(type -> map.put(type, entry)));
		return map;
	}

	@Override
	protected void apply(Map<EntityType<?>, HeadDrop> map, SharedState sharedState) {
		HeadDrop.DROP_MAP.clear();
		HeadDrop.DROP_MAP.putAll(map);
	}
}
