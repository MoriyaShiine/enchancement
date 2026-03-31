/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.chiseledbookshelfpeeking.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.client.gui.hud.ChiseledBookshelfPeekingHudElement;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsExtractorMixin {
	@ModifyReturnValue(method = "guiHeight", at = @At("RETURN"))
	private int enchancement$chiseledBookshelfPeeking(int original) {
		return original - ChiseledBookshelfPeekingHudElement.windowHeightOffset;
	}
}
