/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.util;

import moriyashiine.enchancement.common.component.entity.ExtendedWaterComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin extends ThrownItemEntity {
	public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "applyWater", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/projectile/thrown/PotionEntity;getWorld()Lnet/minecraft/world/World;", ordinal = 0, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private void enchancement$extendedWater(CallbackInfo ci, Box box) {
		getWorld().getNonSpectatingEntities(LivingEntity.class, box).forEach(living -> {
			ExtendedWaterComponent extendedWaterComponent = ModEntityComponents.EXTENDED_WATER.get(living);
			if (extendedWaterComponent.shouldCount()) {
				extendedWaterComponent.markWet();
				extendedWaterComponent.sync();
			}
		});
	}
}
