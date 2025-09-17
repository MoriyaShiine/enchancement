/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.chiseledbookshelfpeeking.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.client.hud.ChiseledBookshelfPeekingHudElement;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DrawContext.class)
public class DrawContextMixin {
	@ModifyReturnValue(method = "getScaledWindowHeight", at = @At("RETURN"))
	private int enchancement$chiseledBookshelfPeeking(int original) {
		return original - ChiseledBookshelfPeekingHudElement.windowHeightOffset;
	}
}
