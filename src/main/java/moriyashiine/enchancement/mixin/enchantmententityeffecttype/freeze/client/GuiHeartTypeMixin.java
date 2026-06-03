/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmententityeffecttype.freeze.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.HeartType.class)
public class GuiHeartTypeMixin {
	@ModifyExpressionValue(method = "forPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isFullyFrozen()Z"))
	private static boolean enchancement$freeze(boolean original, Player player) {
		return original || ModEntityComponents.FROZEN.get(player).isFreezing();
	}
}
