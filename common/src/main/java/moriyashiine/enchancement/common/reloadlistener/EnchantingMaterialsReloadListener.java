package moriyashiine.enchancement.common.reloadlistener;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.event.internal.SyncEnchantingMaterialMapEvent;
import moriyashiine.enchancement.common.util.enchantment.EnchantingMaterial;
import net.fabricmc.fabric.api.resource.v1.reloader.SimpleReloadListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.scanDirectory;

public class EnchantingMaterialsReloadListener extends SimpleReloadListener<Map<Item, EnchantingMaterial>> {
	public static final String DIRECTORY = Enchancement.MOD_ID + "/enchanting_material";

	@Override
	protected Map<Item, EnchantingMaterial> prepare(SharedState sharedState) {
		Map<Item, EnchantingMaterial> map = new HashMap<>();
		scanDirectory(sharedState, DIRECTORY, EnchantingMaterial.CODEC).forEach((identifier, material) -> BuiltInRegistries.ITEM.getOptional(identifier).ifPresent(item -> map.put(item, material)));
		return map;
	}

	@Override
	protected void apply(Map<Item, EnchantingMaterial> map, SharedState sharedState) {
		EnchantingMaterial.MATERIAL_MAP.clear();
		EnchantingMaterial.MATERIAL_MAP.putAll(map);
		SyncEnchantingMaterialMapEvent.shouldSend = true;
	}
}
