/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.screenhandlers.EnchantingTableScreenHandler;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerScreenHandlerType;

public class ModScreenHandlerTypes {
	public static final ScreenHandlerType<EnchantingTableScreenHandler> ENCHANTING_TABLE = registerScreenHandlerType("enchanting_table", new ScreenHandlerType<>(EnchantingTableScreenHandler::new, FeatureFlags.VANILLA_FEATURES));

	public static void init() {
	}
}
