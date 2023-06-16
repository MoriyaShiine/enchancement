/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.client.event;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.brimstone.client.InGameHudAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;


@Environment(EnvType.CLIENT)
public class BrimstoneRenderEvent implements HudRenderCallback {
	public static int forcedHeight = -1, health = -1;

	@Override
	public void onHudRender(DrawContext context, float tickDelta) {
		MinecraftClient minecraft = MinecraftClient.getInstance();
		if (EnchancementUtil.hasEnchantment(ModEnchantments.BRIMSTONE, minecraft.player.getActiveItem())) {
			int scaledWidth = minecraft.getWindow().getScaledWidth(), scaledHeight = minecraft.getWindow().getScaledHeight();
			forcedHeight = (scaledHeight / 2) + 6;
			((InGameHudAccessor) minecraft.inGameHud).enchancement$renderHealthBar(context, minecraft.player, (scaledWidth / 2) - 25, forcedHeight, 1, -1, 12, health, health, 0, false);
			forcedHeight = -1;
		} else {
			health = -1;
		}
	}
}
