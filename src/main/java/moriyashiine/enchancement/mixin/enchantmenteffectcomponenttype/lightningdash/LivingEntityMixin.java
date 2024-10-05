/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.lightningdash;

import moriyashiine.enchancement.common.component.entity.LightningDashComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private float enchancement$lightningDash(float value, DamageSource source) {
		if (source.isIn(DamageTypeTags.IS_FALL)) {
			LightningDashComponent lightningDashComponent = ModEntityComponents.LIGHTNING_DASH.get(this);
			if (lightningDashComponent.isSmashing() && lightningDashComponent.hitNoEntity()) {
				return value / 2;
			}
		}
		return value;
	}
}
