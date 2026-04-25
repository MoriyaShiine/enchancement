/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.slide;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.SlideComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyArg(method = "moveRelative", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getInputVector(Lnet/minecraft/world/phys/Vec3;FF)Lnet/minecraft/world/phys/Vec3;"), index = 2)
	private float enchancement$slide(float value) {
		SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(this);
		if (slideComponent != null && slideComponent.getCachedYRot() != 0) {
			return slideComponent.getCachedYRot();
		}
		return value;
	}
}
