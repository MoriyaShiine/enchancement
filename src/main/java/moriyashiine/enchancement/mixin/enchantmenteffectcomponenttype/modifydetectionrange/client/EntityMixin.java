/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.modifydetectionrange.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.event.enchantmenteffectcomponenttype.EntityXrayClientEvent;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
	@Unique
	private float detectionRangeModifier = 0;

	@SuppressWarnings("ConstantValue")
	@Inject(method = "baseTick", at = @At("TAIL"))
	private void enchancement$modifyDetectionRange(CallbackInfo ci) {
		if ((Object) this instanceof LivingEntity living && !living.slib$isPlayer()) {
			detectionRangeModifier = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.MODIFY_DETECTION_RANGE, living, (float) 1);
		}
	}

	@ModifyReturnValue(method = "shouldRender", at = @At("RETURN"))
	private boolean enchancement$modifyDetectionRange(boolean original, @Local(name = "distance") double distance) {
		if (EntityXrayClientEvent.xrayDistance == 0 && detectionRangeModifier > 0 && detectionRangeModifier != 1) {
			if (Math.sqrt(distance) > 8 / (1 - detectionRangeModifier)) {
				return false;
			}
		}
		return original;
	}
}
