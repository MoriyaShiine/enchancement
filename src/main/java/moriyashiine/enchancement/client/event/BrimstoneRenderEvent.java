/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LayeredDrawerWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;

public class BrimstoneRenderEvent implements HudLayerRegistrationCallback {
	private static final MinecraftClient minecraft = MinecraftClient.getInstance();
	public static int forcedHeight = -1, health = -1;

	@Override
	public void register(LayeredDrawerWrapper layeredDrawer) {
		layeredDrawer.attachLayerAfter(IdentifiedLayer.CROSSHAIR, Enchancement.id("brimstone"), (context, tickCounter) -> {
			if (EnchantmentHelper.hasAnyEnchantmentsWith(minecraft.player.getActiveItem(), ModEnchantmentEffectComponentTypes.BRIMSTONE)) {
				int scaledWidth = minecraft.getWindow().getScaledWidth(), scaledHeight = minecraft.getWindow().getScaledHeight();
				forcedHeight = (scaledHeight / 2) + 6;
				minecraft.inGameHud.renderHealthBar(context, minecraft.player, (scaledWidth / 2) - 25, forcedHeight, 1, -1, 12, health, health, 0, false);
				forcedHeight = -1;
			} else {
				health = -1;
			}
		});
	}
}
