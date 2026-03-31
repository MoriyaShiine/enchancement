/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.reloadlistener;

import com.mojang.serialization.JsonOps;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.event.internal.SyncEnchantingMaterialMapEvent;
import moriyashiine.enchancement.common.util.enchantment.EnchantingMaterial;
import moriyashiine.strawberrylib.api.module.SLibRegistries;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.SimpleReloadListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class EnchantingMaterialsReloadListener extends SimpleReloadListener<Map<Item, EnchantingMaterial>> {
	public static final String DIRECTORY = Enchancement.MOD_ID + "/enchanting_material";

	@Override
	protected Map<Item, EnchantingMaterial> prepare(SharedState sharedState) {
		Map<Identifier, EnchantingMaterial> unmapped = new HashMap<>();
		SLibRegistries.scanErrorless(DIRECTORY, () -> SimpleJsonResourceReloadListener.scanDirectory(sharedState.resourceManager(), FileToIdConverter.json(DIRECTORY), sharedState.get(ResourceLoader.REGISTRY_LOOKUP_KEY).createSerializationContext(JsonOps.INSTANCE), EnchantingMaterial.CODEC, unmapped));
		Map<Item, EnchantingMaterial> map = new HashMap<>();
		unmapped.forEach((identifier, material) -> BuiltInRegistries.ITEM.getOptional(identifier).ifPresent(item -> map.put(item, material)));
		return map;
	}

	@Override
	protected void apply(Map<Item, EnchantingMaterial> map, SharedState sharedState) {
		EnchantingMaterial.MATERIAL_MAP.clear();
		EnchantingMaterial.MATERIAL_MAP.putAll(map);
		SyncEnchantingMaterialMapEvent.shouldSend = true;
	}
}
