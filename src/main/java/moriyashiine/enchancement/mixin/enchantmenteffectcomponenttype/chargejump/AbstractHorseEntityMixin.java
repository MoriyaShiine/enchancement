/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.chargejump;

import moriyashiine.enchancement.common.enchantment.effect.ChargeJumpEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AbstractHorseEntity.class)
public class AbstractHorseEntityMixin {
	@ModifyVariable(method = "jump", at = @At("HEAD"), argsOnly = true)
	private float enchancement$chargeJump(float value) {
		return value + value * ChargeJumpEffect.getStrength((LivingEntity) (Object) this, 0);
	}
}
