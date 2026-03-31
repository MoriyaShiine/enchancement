/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rapidcrossbowfire;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.CrossbowAttack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowAttack.class)
public class CrossbowAttackMixin<E extends Mob> {
	@Shadow
	private int attackDelay;

	@Inject(method = "crossbowAttack", at = @At("HEAD"))
	private void enchancement$rapidCrossbowFire(E body, LivingEntity target, CallbackInfo ci) {
		if (EnchantmentHelper.has(body.getMainHandItem(), ModEnchantmentEffectComponentTypes.RAPID_CROSSBOW_FIRE) && attackDelay > 5) {
			attackDelay = 5;
		}
	}
}
