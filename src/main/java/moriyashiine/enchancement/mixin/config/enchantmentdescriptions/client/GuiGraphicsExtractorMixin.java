/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.enchantmentdescriptions.client;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.gui.screens.inventory.tooltip.StoredEnchantmentsTooltipComponent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsExtractorMixin {
	@Inject(method = "tooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;getWidth(Lnet/minecraft/client/gui/Font;)I"))
	private void enchancement$enchantmentDescriptions(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, @Nullable Identifier style, CallbackInfo ci, @Local(name = "line") ClientTooltipComponent line) {
		if (line instanceof StoredEnchantmentsTooltipComponent storedEnchantmentsComponent) {
			storedEnchantmentsComponent.cacheDimensions(font, xo, yo, (GuiGraphicsExtractor) (Object) this, positioner);
		}
	}
}
