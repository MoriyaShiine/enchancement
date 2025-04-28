/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.extendwatertime;

import moriyashiine.enchancement.common.component.entity.ExtendedWaterTimeComponent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
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
	private void enchancement$extendWaterTime(HitResult hitResult, CallbackInfo ci) {
		getWorld().getNonSpectatingEntities(LivingEntity.class, getBoundingBox().expand(4, 2, 4)).forEach(living -> {
			if (EnchancementUtil.hasAnyEnchantmentsWith(living, ModEnchantmentEffectComponentTypes.EXTEND_WATER_TIME)) {
				ExtendedWaterTimeComponent extendedWaterTimeComponent = ModEntityComponents.EXTENDED_WATER_TIME.get(living);
				extendedWaterTimeComponent.markWet(300);
				extendedWaterTimeComponent.sync();
			}
		});
	}
}
