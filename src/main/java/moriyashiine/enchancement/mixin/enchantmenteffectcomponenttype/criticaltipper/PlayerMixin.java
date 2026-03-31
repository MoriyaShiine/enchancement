/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.criticaltipper;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype.CriticalTipperEvent;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public class PlayerMixin {
	@WrapOperation(method = "attackVisualEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;crit(Lnet/minecraft/world/entity/Entity;)V"))
	private void enchancement$criticalTipper(Player instance, Entity entity, Operation<Void> original) {
		if (CriticalTipperEvent.particleType != null) {
			if (!entity.level().isClientSide()) {
				SLibUtils.addTrackingEmitter(entity, CriticalTipperEvent.particleType);
			}
			CriticalTipperEvent.particleType = null;
		} else {
			original.call(instance, entity);
		}
	}
}
