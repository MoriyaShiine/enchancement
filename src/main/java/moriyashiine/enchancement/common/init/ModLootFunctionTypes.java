/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.init;

import com.mojang.serialization.MapCodec;
import moriyashiine.enchancement.common.world.level.storage.loot.functions.StoreItemEnchantmentsLootFunction;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerLootFunctionType;

public class ModLootFunctionTypes {
	public static final MapCodec<StoreItemEnchantmentsLootFunction> STORE_ITEM_ENCHANTMENTS = registerLootFunctionType("store_item_enchantments", StoreItemEnchantmentsLootFunction.CODEC);

	public static void init() {
	}
}
