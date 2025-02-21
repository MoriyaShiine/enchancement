/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.slide;

import moriyashiine.enchancement.common.component.entity.SlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.payload.StopSlidingC2SPayload;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyVariable(method = "jump", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/LivingEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
	private Vec3d enchancement$slide(Vec3d value) {
		SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(this);
		if (slideComponent != null && slideComponent.isSliding()) {
			slideComponent.stopSliding();
			//noinspection ConstantValue
			if ((Object) this instanceof PlayerEntity player && player.getWorld().isClient) {
				StopSlidingC2SPayload.send();
			}
			return value.multiply(slideComponent.getJumpBonus(), 1, slideComponent.getJumpBonus());
		}
		return value;
	}
}
