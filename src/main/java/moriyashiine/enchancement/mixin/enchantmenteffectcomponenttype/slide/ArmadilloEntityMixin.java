/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.slide;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ArmadilloEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ArmadilloEntity.class)
public class ArmadilloEntityMixin {
	@ModifyExpressionValue(method = "isEntityThreatening", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSprinting()Z"))
	private boolean enchancement$slide(boolean original, LivingEntity entity) {
		return original || ModEntityComponents.SLIDE.get(entity).isSliding();
	}
}
