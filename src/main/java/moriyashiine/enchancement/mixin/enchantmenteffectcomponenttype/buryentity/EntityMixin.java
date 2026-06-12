/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.buryentity;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.BuryEntityComponent;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true)
	private Vec3 enchancement$buryEntity(Vec3 delta, MoverType moverType) {
		if (moverType == MoverType.SELF) {
			BuryEntityComponent buryEntity = EnchancementEntityComponents.BURY_ENTITY.getNullable(this);
			if (buryEntity != null && buryEntity.getBuryPos() != null) {
				return Vec3.ZERO;
			}
		}
		return delta;
	}
}
