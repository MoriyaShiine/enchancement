/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.gale;

import moriyashiine.enchancement.common.component.entity.GaleComponent;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyVariable(method = "handleFallDamage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float enchancement$gale(float value) {
		GaleComponent galeComponent = ModEntityComponents.GALE.getNullable(this);
		if (galeComponent != null && galeComponent.hasGale()) {
			value = Math.max(0, value - 2);
		}
		return value;
	}
}
