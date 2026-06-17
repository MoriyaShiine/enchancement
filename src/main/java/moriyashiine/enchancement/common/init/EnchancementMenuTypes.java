/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.world.inventory.OverhauledEnchantmentMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerMenuType;

public class EnchancementMenuTypes {
	public static final MenuType<OverhauledEnchantmentMenu> OVERHAULED_ENCHANTING_TABLE = registerMenuType("overhauled_enchanting_table", new MenuType<>(OverhauledEnchantmentMenu::new, FeatureFlags.VANILLA_SET));

	public static void init() {
	}
}
