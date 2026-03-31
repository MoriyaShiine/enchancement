/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract boolean isAutoSpinAttack();

	public LivingEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@ModifyExpressionValue(method = "shouldTravelInFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z"))
	private boolean enchancement$rebalanceEnchantments(boolean original) {
		if (ModConfig.rebalanceEnchantments && isAutoSpinAttack()) {
			return false;
		}
		return original;
	}

	@ModifyReturnValue(method = "getEffectiveGravity", at = @At("RETURN"))
	private double enchancement$rebalanceEnchantments(double original) {
		if (ModConfig.rebalanceEnchantments && isAutoSpinAttack() && isInWater()) {
			return original / 8;
		}
		return original;
	}
}
