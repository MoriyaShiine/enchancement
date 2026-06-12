/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.brimstone.client;

import moriyashiine.enchancement.client.gui.hud.BrimstoneHudElement;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Hud.class)
public class HudMixin {
	@ModifyArg(method = "extractHearts", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Hud;extractHeart(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Hud$HeartType;IIZZZ)V"), index = 3)
	private int enchancement$brimstone(int yo) {
		if (BrimstoneHudElement.forcedHeight >= 0) {
			return BrimstoneHudElement.forcedHeight;
		}
		return yo;
	}
}
