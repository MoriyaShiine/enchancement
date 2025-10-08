/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.criticaltipper;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype.CriticalTipperEvent;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addCritParticles(Lnet/minecraft/entity/Entity;)V"))
	private void enchancement$criticalTipper(PlayerEntity instance, Entity target, Operation<Void> original) {
		if (CriticalTipperEvent.particleType != null) {
			if (!target.getEntityWorld().isClient()) {
				SLibUtils.addEmitterParticle(target, CriticalTipperEvent.particleType);
			}
			CriticalTipperEvent.particleType = null;
		} else {
			original.call(instance, target);
		}
	}
}
