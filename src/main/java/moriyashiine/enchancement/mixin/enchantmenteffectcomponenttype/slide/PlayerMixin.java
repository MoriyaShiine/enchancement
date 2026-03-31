/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.slide;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Player.class)
public class PlayerMixin {
	@ModifyReturnValue(method = "isStayingOnGroundSurface", at = @At("RETURN"))
	private boolean enchancement$slide(boolean original) {
		return original && !ModEntityComponents.SLIDE.get(this).isSliding();
	}

	@ModifyArg(method = "updatePlayerPose", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setPose(Lnet/minecraft/world/entity/Pose;)V"))
	private Pose enchancement$slide(Pose value) {
		if (value == Pose.STANDING && ModEntityComponents.SLIDE.get(this).shouldCrawl()) {
			return Pose.SWIMMING;
		}
		return value;
	}
}
