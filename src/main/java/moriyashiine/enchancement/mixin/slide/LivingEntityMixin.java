/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.slide;

import moriyashiine.enchancement.common.component.entity.SlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Shadow
	private float leaningPitch;

	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private float enchancement$slide(float value, DamageSource source) {
		if (source.getSource() != null) {
			SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(this);
			if (slideComponent != null && slideComponent.isSliding()) {
				return value * 1.5F;
			}
		}
		return value;
	}

	@ModifyVariable(method = "jump", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/LivingEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
	private Vec3d enchancement$slide(Vec3d value) {
		SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(this);
		if (slideComponent != null && slideComponent.isSliding()) {
			slideComponent.setVelocity(Vec3d.ZERO);
			return value.multiply(slideComponent.getJumpBonus());
		}
		return value;
	}

	@Inject(method = "updateLeaningPitch", at = @At("TAIL"))
	private void enchancement$slide(CallbackInfo ci) {
		SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(this);
		if (slideComponent != null && slideComponent.isSliding()) {
			leaningPitch = 1;
		}
	}
}
