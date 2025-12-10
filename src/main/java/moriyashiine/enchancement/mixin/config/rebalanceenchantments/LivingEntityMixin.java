/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract boolean isUsingRiptide();

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyExpressionValue(method = "isTravellingInFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isTouchingWater()Z"))
	private boolean enchancement$rebalanceEnchantments(boolean original) {
		if (ModConfig.rebalanceEnchantments && isUsingRiptide()) {
			return false;
		}
		return original;
	}

	@ModifyReturnValue(method = "getEffectiveGravity", at = @At("RETURN"))
	private double enchancement$rebalanceEnchantments(double original) {
		if (ModConfig.rebalanceEnchantments && isUsingRiptide() && isTouchingWater()) {
			return original / 8;
		}
		return original;
	}
}
