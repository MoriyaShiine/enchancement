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
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin extends ThrownItemEntity {
	public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "onCollision", at = @At("TAIL"))
	private void enchancement$extendedWater(HitResult hitResult, CallbackInfo ci) {
		getWorld().getNonSpectatingEntities(LivingEntity.class, getBoundingBox().expand(4, 2, 4)).forEach(living -> {
			ExtendedWaterComponent extendedWaterComponent = ModEntityComponents.EXTENDED_WATER.get(living);
			if (extendedWaterComponent.shouldCount()) {
				extendedWaterComponent.markWet(600);
				extendedWaterComponent.sync();
			}
		});
	}
}
