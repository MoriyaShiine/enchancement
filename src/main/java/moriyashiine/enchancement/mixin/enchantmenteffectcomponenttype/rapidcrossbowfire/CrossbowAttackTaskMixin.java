/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rapidcrossbowfire;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.CrossbowAttackTask;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowAttackTask.class)
public class CrossbowAttackTaskMixin<E extends MobEntity> {
	@Shadow
	private int chargingCooldown;

	@Inject(method = "tickState", at = @At("HEAD"))
	private void enchancement$rapidCrossbowFire(E entity, LivingEntity target, CallbackInfo ci) {
		if (EnchantmentHelper.hasAnyEnchantmentsWith(entity.getMainHandStack(), ModEnchantmentEffectComponentTypes.RAPID_CROSSBOW_FIRE) && chargingCooldown > 5) {
			chargingCooldown = 5;
		}
	}
}
