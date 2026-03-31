/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.reloadlistener;

import com.mojang.serialization.JsonOps;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.util.enchantment.BaseBlock;
import moriyashiine.strawberrylib.api.module.SLibRegistries;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.SimpleReloadListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class BaseBlocksReloadListener extends SimpleReloadListener<Map<Block, BaseBlock>> {
	public static final String DIRECTORY = Enchancement.MOD_ID + "/base_blocks";

	@Override
	protected Map<Block, BaseBlock> prepare(SharedState sharedState) {
		Map<Identifier, BaseBlock> unmapped = new HashMap<>();
		SLibRegistries.scanErrorless(DIRECTORY, () -> SimpleJsonResourceReloadListener.scanDirectory(sharedState.resourceManager(), FileToIdConverter.json(DIRECTORY), sharedState.get(ResourceLoader.REGISTRY_LOOKUP_KEY).createSerializationContext(JsonOps.INSTANCE), BaseBlock.CODEC, unmapped));
		Map<Block, BaseBlock> map = new HashMap<>();
		unmapped.forEach((identifier, entry) -> BuiltInRegistries.BLOCK.getOptional(identifier).ifPresent(block -> map.put(block, entry)));
		return map;
	}

	@Override
	protected void apply(Map<Block, BaseBlock> map, SharedState sharedState) {
		BaseBlock.BLOCK_MAP.clear();
		BaseBlock.BLOCK_MAP.putAll(map);
	}
}
