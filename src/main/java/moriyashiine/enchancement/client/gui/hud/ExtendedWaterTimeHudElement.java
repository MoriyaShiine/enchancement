/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.gui.hud;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.ExtendedWaterTimeComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ExtendedWaterTimeHudElement implements HudElement {
	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
		Player player = Minecraft.getInstance().player;
		if (player != null && !player.isSpectator()) {
			ExtendedWaterTimeComponent extendedWaterTimeComponent = ModEntityComponents.EXTENDED_WATER_TIME.get(player.getControlledVehicle() instanceof LivingEntity living ? living : player);
			int ticksWet = extendedWaterTimeComponent.getTicksWet();
			if (ticksWet > 0) {
				int x = graphics.guiWidth() / 2 - 11, y = graphics.guiHeight() / 2 + 5;
				float progress = Math.min(1, Mth.lerp((extendedWaterTimeComponent.getTicksWet() + 1) / (float) extendedWaterTimeComponent.getLastMarked(), 0, 1));
				if (progress > 0) {
					graphics.fill(x, y, x + 22, y + 2, 0xFF052463);
					graphics.fill(x, y, x + (int) (22 * progress), y + 2, 0xFF1C53A8);
				}
			}
		}
	}
}
