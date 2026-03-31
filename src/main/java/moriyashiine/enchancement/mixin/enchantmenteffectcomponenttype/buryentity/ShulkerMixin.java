/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.buryentity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Shulker.class)
public abstract class ShulkerMixin extends Entity {
	public ShulkerMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@ModifyReturnValue(method = "makeBoundingBox", at = @At("RETURN"))
	private AABB enchancement$buryEntity(AABB original) {
		if (tickCount > 0 && ModEntityComponents.BURY_ENTITY.get(this).getBuryPos() != null) {
			return original.contract(0, 0.5, 0);
		}
		return original;
	}
}
