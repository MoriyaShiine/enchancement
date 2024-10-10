/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.walljump;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.component.entity.WallJumpComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@WrapOperation(method = "computeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D", ordinal = 0))
	private double enchancement$wallJump(LivingEntity instance, RegistryEntry<EntityAttribute> attribute, Operation<Double> original) {
		double value = original.call(instance, attribute);
		WallJumpComponent wallJumpComponent = ModEntityComponents.WALL_JUMP.getNullable(this);
		if (wallJumpComponent != null && wallJumpComponent.hasJumped()) {
			return value + 8;
		}
		return value;
	}
}
