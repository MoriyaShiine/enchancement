/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.api.event.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public interface HudAddition extends HudRenderCallback {
	@Override
	default void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
		if (!MinecraftClient.getInstance().options.hudHidden) {
			render(drawContext, tickCounter);
		}
	}

	void render(DrawContext drawContext, RenderTickCounter tickCounter);
}
