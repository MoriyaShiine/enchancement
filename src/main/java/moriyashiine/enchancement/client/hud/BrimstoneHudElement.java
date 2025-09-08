/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.client.hud;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.enchantment.EnchantmentHelper;

public class BrimstoneHudElement implements HudElement {
	private static final MinecraftClient client = MinecraftClient.getInstance();
	public static int forcedHeight = -1, health = -1;

	@Override
	public void render(DrawContext context, RenderTickCounter tickCounter) {
		if (client.player != null && EnchantmentHelper.hasAnyEnchantmentsWith(client.player.getActiveItem(), ModEnchantmentEffectComponentTypes.BRIMSTONE)) {
			int scaledWidth = client.getWindow().getScaledWidth(), scaledHeight = client.getWindow().getScaledHeight();
			forcedHeight = (scaledHeight / 2) + 6;
			client.inGameHud.renderHealthBar(context, client.player, (scaledWidth / 2) - 25, forcedHeight, 1, -1, 12, health, health, 0, false);
			forcedHeight = -1;
		} else {
			health = -1;
		}
	}
}
