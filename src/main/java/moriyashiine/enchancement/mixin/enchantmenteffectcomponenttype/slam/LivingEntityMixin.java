/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.slam;

import moriyashiine.enchancement.common.component.entity.SlamComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
	private void enchancement$slam(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		SlamComponent slamComponent = ModEntityComponents.SLAM.getNullable(this);
		if (slamComponent != null && slamComponent.isSlamming()) {
			cir.setReturnValue(false);
		}
	}

	@ModifyVariable(method = "jump", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/LivingEntity;getJumpVelocity()F"))
	private float enchancement$slam(float value) {
		SlamComponent slamComponent = ModEntityComponents.SLAM.getNullable(this);
		if (slamComponent != null) {
			return value * slamComponent.getJumpBoostStrength();
		}
		return value;
	}
}
