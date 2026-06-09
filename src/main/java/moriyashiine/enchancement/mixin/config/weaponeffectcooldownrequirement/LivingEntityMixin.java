/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.weaponeffectcooldownrequirement;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, priority = 500)
public class LivingEntityMixin {
	@Inject(method = "getSecondsToDisableBlocking", at = @At("HEAD"), cancellable = true)
	private void enchancement$weaponEffectCooldownRequirement(CallbackInfoReturnable<Float> cir) {
		if (!EnchancementUtil.shouldApplyWeaponEffect()) {
			cir.setReturnValue(0F);
		}
	}
}
