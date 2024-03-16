/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.gale;

import moriyashiine.enchancement.common.component.entity.GaleComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
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
			value = Math.max(0, value - (galeComponent.getGaleLevel() - galeComponent.getJumpsLeft()));
		}
		return value;
	}
}
