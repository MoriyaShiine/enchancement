/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.world.inventory.ModEnchantmentMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerMenuType;

public class ModMenuTypes {
	public static final MenuType<ModEnchantmentMenu> ENCHANTING_TABLE = registerMenuType("enchanting_table", new MenuType<>(ModEnchantmentMenu::new, FeatureFlags.VANILLA_SET));

	public static void init() {
	}
}
