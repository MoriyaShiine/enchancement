/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.lightningdash;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.LightningDashComponent;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyReturnValue(method = "getGravity", at = @At("RETURN"))
	private double enchancement$lightningDash(double original) {
		LightningDashComponent lightningDash = EnchancementEntityComponents.LIGHTNING_DASH.getNullable(this);
		if (lightningDash != null && lightningDash.isFloating()) {
			return 0;
		}
		return original;
	}
}
