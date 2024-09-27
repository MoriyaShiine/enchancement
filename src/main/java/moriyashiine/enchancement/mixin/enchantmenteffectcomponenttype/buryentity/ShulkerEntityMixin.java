/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.buryentity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShulkerEntity.class)
public abstract class ShulkerEntityMixin extends Entity {
	public ShulkerEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyReturnValue(method = "calculateBoundingBox()Lnet/minecraft/util/math/Box;", at = @At("RETURN"))
	private Box enchancement$buryEntity(Box original) {
		if (age > 0 && ModEntityComponents.BURY_ENTITY.get(this).getBuryPos() != null) {
			return original.shrink(0, 0.5, 0);
		}
		return original;
	}
}
