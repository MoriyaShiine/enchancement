/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.client.gui.hud;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.RotationBurstComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class RotationBurstHudElement implements HudElement {
	private static final Identifier BACKGROUND_TEXTURE = Enchancement.id("hud/burst_background");
	private static final Identifier PROGRESS_TEXTURE = Enchancement.id("hud/burst_progress");

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
		Player player = Minecraft.getInstance().player;
		if (player != null && !player.isSpectator()) {
			RotationBurstComponent rotationBurstComponent = ModEntityComponents.ROTATION_BURST.get(player.getControlledVehicle() instanceof LivingEntity living ? living : player);
			if (rotationBurstComponent.hasEffect() && rotationBurstComponent.getCooldown() > 0) {
				int x = graphics.guiWidth() / 2 - 5, y = graphics.guiHeight() / 2 + 18;
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, x, y, 10, 4);
				if (rotationBurstComponent.getCooldown() < rotationBurstComponent.getLastCooldown()) {
					graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_TEXTURE, 10, 4, 0, 0, x, y, (int) (11 - (rotationBurstComponent.getCooldown() / (float) rotationBurstComponent.getLastCooldown()) * 10), 4);
				}
			}
		}
	}
}
