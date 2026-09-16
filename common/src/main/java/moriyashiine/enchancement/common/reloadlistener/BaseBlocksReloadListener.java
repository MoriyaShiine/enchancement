package moriyashiine.enchancement.common.reloadlistener;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.util.enchantment.BaseBlock;
import net.fabricmc.fabric.api.resource.v1.reloader.SimpleReloadListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.scanDirectory;

public class BaseBlocksReloadListener extends SimpleReloadListener<Map<Block, BaseBlock>> {
	public static final String DIRECTORY = Enchancement.MOD_ID + "/base_blocks";

	@Override
	protected Map<Block, BaseBlock> prepare(SharedState sharedState) {
		Map<Block, BaseBlock> map = new HashMap<>();
		scanDirectory(sharedState, DIRECTORY, BaseBlock.CODEC).forEach((identifier, entry) -> BuiltInRegistries.BLOCK.getOptional(identifier).ifPresent(block -> map.put(block, entry)));
		return map;
	}

	@Override
	protected void apply(Map<Block, BaseBlock> map, SharedState sharedState) {
		BaseBlock.BLOCK_MAP.clear();
		BaseBlock.BLOCK_MAP.putAll(map);
	}
}
