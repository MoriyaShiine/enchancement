/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.fluidwalking;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyReturnValue(method = "canWalkOnFluid", at = @At("RETURN"))
	protected boolean enchancement$fluidWalking(boolean original) {
		return original;
	}

	@Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
	private void enchancement$fluidWalking(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		if (fallDistance > getSafeFallDistance() && !getWorld().getFluidState(getSteppingPos()).isEmpty() && EnchancementUtil.hasAnyEnchantmentsWith(this, ModEnchantmentEffectComponentTypes.FLUID_WALKING)) {
			cir.setReturnValue(false);
		}
	}
}
