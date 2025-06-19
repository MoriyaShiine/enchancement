/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.util.client;

import moriyashiine.enchancement.client.EnchancementClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = DrawContext.class, priority = 1001)
public abstract class DrawContextMixin {
	@Shadow
	public abstract void drawTooltipImmediately(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, @Nullable Identifier texture);

	@Inject(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;Lnet/minecraft/util/Identifier;Z)V", at = @At("HEAD"), cancellable = true)
	private void enchancement$drawTooltipsImmediately(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, @Nullable Identifier texture, boolean focused, CallbackInfo ci) {
		if (EnchancementClient.drawTooltipsImmediately) {
			EnchancementClient.drawTooltipsImmediately = false;
			drawTooltipImmediately(textRenderer, components, x, y, positioner, texture);
			ci.cancel();
		}
	}
}
