/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.slide;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@ModifyReturnValue(method = "clipAtLedge", at = @At("RETURN"))
	private boolean enchancement$slide(boolean original) {
		return original && !ModEntityComponents.SLIDE.get(this).isSliding();
	}

	@ModifyArg(method = "updatePose", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setPose(Lnet/minecraft/entity/EntityPose;)V"))
	private EntityPose enchancement$slide(EntityPose value) {
		if (value == EntityPose.STANDING && ModEntityComponents.SLIDE.get(this).isSliding()) {
			return EntityPose.SWIMMING;
		}
		return value;
	}
}
