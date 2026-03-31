/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.entity.torch;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.common.init.ModEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile {
	public AbstractArrowMixin(EntityType<? extends Projectile> type, Level level) {
		super(type, level);
	}

	@ModifyExpressionValue(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;isOnFire()Z"))
	private boolean enchancement$torch(boolean value) {
		if (getType() == ModEntityTypes.TORCH) {
			return false;
		}
		return value;
	}

	@WrapWithCondition(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setArrowCount(I)V"))
	private boolean enchancement$torch(LivingEntity instance, int count) {
		return getType() != ModEntityTypes.TORCH;
	}
}
