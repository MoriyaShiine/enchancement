/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.gui.hud;

import moriyashiine.enchancement.common.init.EnchancementEnchantmentEffectComponentTypes;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class BrimstoneHudElement implements HudElement {
	public static int forcedHeight = -1, health = -1;

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
		Minecraft client = Minecraft.getInstance();
		Player player = client.player;
		if (player != null && EnchantmentHelper.has(player.getUseItem(), EnchancementEnchantmentEffectComponentTypes.BRIMSTONE)) {
			int scaledWidth = client.getWindow().getGuiScaledWidth(), scaledHeight = client.getWindow().getGuiScaledHeight();
			forcedHeight = (scaledHeight / 2) + 6;
			client.gui.hud.extractHearts(graphics, player, (scaledWidth / 2) - 25, forcedHeight, 1, -1, 12, health, health, 0, false);
			forcedHeight = -1;
		} else {
			health = -1;
		}
	}
}
