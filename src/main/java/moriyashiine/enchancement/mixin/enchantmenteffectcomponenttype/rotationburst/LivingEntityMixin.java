/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rotationburst;

import moriyashiine.enchancement.common.component.entity.RotationBurstComponent;
import moriyashiine.enchancement.common.enchantment.effect.RotationBurstEffect;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyVariable(method = "jump", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/LivingEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
	private Vec3d enchancement$rotationBurst(Vec3d value) {
		RotationBurstComponent rotationBurstComponent = ModEntityComponents.ROTATION_BURST.getNullable(this);
		if (rotationBurstComponent != null && rotationBurstComponent.shouldWavedash()) {
			rotationBurstComponent.setCooldown(0);
			return value.multiply(RotationBurstEffect.getWavedashStrength((LivingEntity) (Object) this));
		}
		return value;
	}
}
