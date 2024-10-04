/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rotationmovementburst;

import moriyashiine.enchancement.common.component.entity.RotationMovementBurstComponent;
import moriyashiine.enchancement.common.enchantment.effect.RotationMovementBurstEffect;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyVariable(method = "jump", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/LivingEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
	private Vec3d enchancement$rotationMovementBurst(Vec3d value) {
		RotationMovementBurstComponent rotationMovementBurstComponent = ModEntityComponents.ROTATION_MOVEMENT_BURST.getNullable(this);
		if (rotationMovementBurstComponent != null && rotationMovementBurstComponent.shouldWavedash()) {
			rotationMovementBurstComponent.setCooldown(0);
			return value.multiply(RotationMovementBurstEffect.getWavedashStrength((LivingEntity) (Object) this));
		}
		return value;
	}
}
