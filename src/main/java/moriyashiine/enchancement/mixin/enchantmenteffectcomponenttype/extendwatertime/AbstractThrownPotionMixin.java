/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.extendwatertime;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.ExtendedWaterTimeComponent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractThrownPotion.class)
public abstract class AbstractThrownPotionMixin extends ThrowableItemProjectile {
	public AbstractThrownPotionMixin(EntityType<? extends ThrowableItemProjectile> type, Level level) {
		super(type, level);
	}

	@Inject(method = "onHit", at = @At("TAIL"))
	private void enchancement$extendWaterTime(HitResult hitResult, CallbackInfo ci) {
		level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(4, 2, 4)).forEach(living -> {
			if (EnchancementUtil.hasAnyEnchantmentsWith(living, ModEnchantmentEffectComponentTypes.EXTEND_WATER_TIME)) {
				ExtendedWaterTimeComponent extendedWaterTimeComponent = ModEntityComponents.EXTENDED_WATER_TIME.get(living);
				extendedWaterTimeComponent.markWet(300);
				extendedWaterTimeComponent.sync();
			}
		});
	}
}
