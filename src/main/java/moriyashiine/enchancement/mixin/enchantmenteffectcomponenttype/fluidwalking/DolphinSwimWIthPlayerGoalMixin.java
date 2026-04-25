/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.fluidwalking;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype.FluidWalkingEvent;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Dolphin.DolphinSwimWithPlayerGoal.class)
public class DolphinSwimWIthPlayerGoalMixin {
	@Shadow
	private @Nullable Player player;

	@ModifyExpressionValue(method = "canUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSwimming()Z"))
	private boolean enchancement$fluidWalking(boolean original) {
		return original || FluidWalkingEvent.DolphinsGrace.shouldApply(player);
	}
}
