/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.slide.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
	@ModifyReturnValue(method = "isSprintingPossible", at = @At("RETURN"))
	private boolean enchancement$slide(boolean original) {
		return original && !ModEntityComponents.SLIDE.get(this).isSliding();
	}
}
