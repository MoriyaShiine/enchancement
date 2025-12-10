/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.fluidwalking;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.component.entity.BoostInFluidComponent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.SubmersionGate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyExpressionValue(method = "isTravellingInFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;canWalkOnFluid(Lnet/minecraft/fluid/FluidState;)Z"))
	protected boolean enchancement$fluidWalking(boolean original) {
		return original || EnchancementUtil.shouldFluidWalk(this);
	}

	@ModifyReturnValue(method = "getEffectiveGravity", at = @At("RETURN"))
	private double enchancement$fluidWalking(double original) {
		if (EnchancementUtil.hasAnyEnchantmentsWith(this, ModEnchantmentEffectComponentTypes.FLUID_WALKING) && SLibUtils.isSubmerged(this, SubmersionGate.ALL)) {
			@Nullable BoostInFluidComponent boostInFluidComponent = ModEntityComponents.BOOST_IN_FLUID.getNullable(this);
			if (boostInFluidComponent == null || !boostInFluidComponent.isBoosting()) {
				return original / 3;
			}
		}
		return original;
	}
}
