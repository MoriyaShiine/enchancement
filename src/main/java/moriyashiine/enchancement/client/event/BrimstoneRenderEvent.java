/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.event;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.util.accessor.client.InGameHudAccessor;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class BrimstoneRenderEvent implements HudRenderCallback {
	private static final MinecraftClient minecraft = MinecraftClient.getInstance();
	public static int forcedHeight = -1, health = -1;

	@Override
	public void onHudRender(DrawContext drawContext, float tickDelta) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.BRIMSTONE, minecraft.player.getActiveItem())) {
			int scaledWidth = minecraft.getWindow().getScaledWidth(), scaledHeight = minecraft.getWindow().getScaledHeight();
			forcedHeight = (scaledHeight / 2) + 6;
			((InGameHudAccessor) minecraft.inGameHud).enchancement$renderHealthBar(drawContext, minecraft.player, (scaledWidth / 2) - 25, forcedHeight, 1, -1, 12, health, health, 0, false);
			forcedHeight = -1;
		} else {
			health = -1;
		}
	}
}
