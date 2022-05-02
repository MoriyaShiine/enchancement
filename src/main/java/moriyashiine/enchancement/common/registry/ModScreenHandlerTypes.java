package moriyashiine.enchancement.common.registry;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.screenhandlers.EnchantingTableScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModScreenHandlerTypes {
	public static final ScreenHandlerType<EnchantingTableScreenHandler> ENCHANTING_TABLE = new ScreenHandlerType<>(EnchantingTableScreenHandler::new);

	public static void init() {
		Registry.register(Registry.SCREEN_HANDLER, new Identifier(Enchancement.MOD_ID, "enchanting_table"), ENCHANTING_TABLE);
	}
}
