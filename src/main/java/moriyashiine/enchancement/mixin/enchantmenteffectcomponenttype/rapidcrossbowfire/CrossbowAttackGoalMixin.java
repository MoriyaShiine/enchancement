/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rapidcrossbowfire;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.ai.goal.CrossbowAttackGoal;
import net.minecraft.entity.mob.HostileEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowAttackGoal.class)
public class CrossbowAttackGoalMixin<T extends HostileEntity & CrossbowUser> {
	@Shadow
	@Final
	private T actor;

	@Shadow
	private int chargedTicksLeft;

	@Inject(method = "tick", at = @At("HEAD"))
	private void enchancement$rapidCrossbowFire(CallbackInfo ci) {
		if (EnchantmentHelper.hasAnyEnchantmentsWith(actor.getMainHandStack(), ModEnchantmentEffectComponentTypes.RAPID_CROSSBOW_FIRE) && chargedTicksLeft > 5) {
			chargedTicksLeft = 5;
		}
	}
}
