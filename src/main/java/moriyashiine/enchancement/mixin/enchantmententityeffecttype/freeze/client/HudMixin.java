/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmententityeffecttype.freeze.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public class HudMixin {
	@ModifyExpressionValue(method = "extractCameraOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getTicksFrozen()I"))
	private int enchancement$freeze(int original, @Local(name = "player") LocalPlayer player) {
		return Math.max(original, EnchancementEntityComponents.FROZEN.get(player).getFreezeTicks());
	}

	@ModifyExpressionValue(method = "extractCameraOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getPercentFrozen()F"))
	private float enchancement$freeze(float original, @Local(name = "player") LocalPlayer player) {
		return Math.max(original, EnchancementEntityComponents.FROZEN.get(player).getFreezePercentage());
	}
}
