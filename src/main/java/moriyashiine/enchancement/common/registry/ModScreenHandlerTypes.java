package moriyashiine.enchancement.common.registry;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.screenhandlers.EnchantingTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlerTypes {
	public static ScreenHandlerType<EnchantingTableScreenHandler> ENCHANTING_TABLE;

	public static void init() {
		ENCHANTING_TABLE = ScreenHandlerRegistry.registerSimple(new Identifier(Enchancement.MOD_ID, "enchanting_table"), EnchantingTableScreenHandler::new);
	}
}
