/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.lightningdash;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.component.entity.LightningDashComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyReturnValue(method = "getFinalGravity", at = @At("RETURN"))
	private double enchancement$lightningDash(double original) {
		LightningDashComponent lightningDashComponent = ModEntityComponents.LIGHTNING_DASH.getNullable(this);
		if (lightningDashComponent != null && lightningDashComponent.isFloating()) {
			return 0;
		}
		return original;
	}
}
