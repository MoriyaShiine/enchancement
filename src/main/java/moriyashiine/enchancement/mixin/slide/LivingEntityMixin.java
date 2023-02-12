/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.slide;

import moriyashiine.enchancement.common.component.entity.SlideComponent;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
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

	@ModifyVariable(method = "handleFallDamage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float enchancement$slide(float value) {
		SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(this);
		if (slideComponent != null && slideComponent.isSliding()) {
			return Math.max(0, value - 6);
		}
		return value;
	}

	@ModifyVariable(method = "jump", at = @At("STORE"))
	private double enchancement$slide(double value) {
		SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(this);
		if (slideComponent != null && slideComponent.shouldBoostJump()) {
			slideComponent.setJumpBoostResetTicks(SlideComponent.DEFAULT_JUMP_BOOST_RESET_TICKS);
			return value + ((slideComponent.getTimesJumped() - 1) / 3.75F);
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
