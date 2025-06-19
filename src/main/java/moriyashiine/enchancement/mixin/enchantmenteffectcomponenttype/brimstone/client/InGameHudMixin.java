/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.brimstone.client;

import moriyashiine.enchancement.client.hud.BrimstoneHudElement;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@ModifyArg(method = "renderHealthBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawHeart(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/gui/hud/InGameHud$HeartType;IIZZZ)V"), index = 3)
	private int enchancement$brimstone(int value) {
		if (BrimstoneHudElement.forcedHeight >= 0) {
			return BrimstoneHudElement.forcedHeight;
		}
		return value;
	}
}
