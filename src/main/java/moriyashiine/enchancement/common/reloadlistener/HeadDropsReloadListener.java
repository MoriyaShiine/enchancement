/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.reloadlistener;

import com.mojang.serialization.JsonOps;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.util.enchantment.HeadDrop;
import moriyashiine.strawberrylib.api.module.SLibRegistries;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.SimpleReloadListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class HeadDropsReloadListener extends SimpleReloadListener<Map<EntityType<?>, HeadDrop>> {
	public static final String DIRECTORY = Enchancement.MOD_ID + "/head_drops";

	@Override
	protected Map<EntityType<?>, HeadDrop> prepare(SharedState sharedState) {
		Map<Identifier, HeadDrop> unmapped = new HashMap<>();
		SLibRegistries.scanErrorless(DIRECTORY, () -> SimpleJsonResourceReloadListener.scanDirectory(sharedState.resourceManager(), FileToIdConverter.json(DIRECTORY), sharedState.get(ResourceLoader.REGISTRY_LOOKUP_KEY).createSerializationContext(JsonOps.INSTANCE), HeadDrop.CODEC, unmapped));
		Map<EntityType<?>, HeadDrop> map = new HashMap<>();
		unmapped.forEach((identifier, entry) -> BuiltInRegistries.ENTITY_TYPE.getOptional(identifier).ifPresent(type -> map.put(type, entry)));
		return map;
	}

	@Override
	protected void apply(Map<EntityType<?>, HeadDrop> map, SharedState sharedState) {
		HeadDrop.DROP_MAP.clear();
		HeadDrop.DROP_MAP.putAll(map);
	}
}
