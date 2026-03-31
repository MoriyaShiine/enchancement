/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rapidcrossbowfire;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RangedCrossbowAttackGoal.class)
public class RangedCrossbowAttackGoalMixin<T extends Monster & CrossbowAttackMob> {
	@Shadow
	@Final
	private T mob;

	@Shadow
	private int attackDelay;

	@Inject(method = "tick", at = @At("HEAD"))
	private void enchancement$rapidCrossbowFire(CallbackInfo ci) {
		if (EnchantmentHelper.has(mob.getMainHandItem(), ModEnchantmentEffectComponentTypes.RAPID_CROSSBOW_FIRE) && attackDelay > 5) {
			attackDelay = 5;
		}
	}
}
