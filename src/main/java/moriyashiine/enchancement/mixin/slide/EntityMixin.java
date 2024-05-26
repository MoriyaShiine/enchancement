/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.slide;

import moriyashiine.enchancement.common.component.entity.SlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyVariable(method = "getJumpVelocityMultiplier", at = @At("STORE"), ordinal = 0)
	private float enchancement$slide(float value) {
		SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(this);
		if (slideComponent != null) {
			if (slideComponent.getJumpBonus() > 2) {
				return value * 1.25F;
			} else if (slideComponent.shouldBoostJump()) {
				return value * (1 + 0.5F * slideComponent.getSlideLevel());
			}
		}
		return value;
	}
}
