/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.registry;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.screenhandlers.EnchantingTableScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlerTypes {
	public static final ScreenHandlerType<EnchantingTableScreenHandler> ENCHANTING_TABLE = new ScreenHandlerType<>(EnchantingTableScreenHandler::new, FeatureFlags.VANILLA_FEATURES);

	public static void init() {
		Registry.register(Registries.SCREEN_HANDLER, Enchancement.id("enchanting_table"), ENCHANTING_TABLE);
	}
}
