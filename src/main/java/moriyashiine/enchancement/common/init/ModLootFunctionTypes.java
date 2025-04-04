/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.loot.function.StoreItemEnchantmentsLootFunction;
import net.minecraft.loot.function.LootFunctionType;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerLootFunctionType;

public class ModLootFunctionTypes {
	public static final LootFunctionType<StoreItemEnchantmentsLootFunction> STORE_ITEM_ENCHANTMENTS = registerLootFunctionType("store_item_enchantments", StoreItemEnchantmentsLootFunction.CODEC);

	public static void init() {
	}
}
