/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.buryentity;

import moriyashiine.enchancement.common.component.entity.BuryEntityComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true)
	private Vec3d enchancement$buryEntity(Vec3d value, MovementType movementType) {
		if (movementType == MovementType.SELF) {
			BuryEntityComponent buryEntityComponent = ModEntityComponents.BURY_ENTITY.getNullable(this);
			if (buryEntityComponent != null && buryEntityComponent.getBuryPos() != null) {
				return Vec3d.ZERO;
			}
		}
		return value;
	}
}
