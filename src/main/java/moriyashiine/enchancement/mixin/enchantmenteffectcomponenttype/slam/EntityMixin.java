/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.slam;

import moriyashiine.enchancement.common.component.entity.SlamComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyVariable(method = "getJumpVelocityMultiplier", at = @At("STORE"), ordinal = 0)
	private float enchancement$slam(float value) {
		SlamComponent slamComponent = ModEntityComponents.SLAM.getNullable(this);
		if (slamComponent != null && slamComponent.shouldBoostJump()) {
			return value * (1 + slamComponent.getStrength());
		}
		return value;
	}
}
