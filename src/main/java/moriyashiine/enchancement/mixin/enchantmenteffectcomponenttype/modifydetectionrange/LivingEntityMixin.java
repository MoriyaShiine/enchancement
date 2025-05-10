/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.modifydetectionrange;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.tag.ModEntityTypeTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyReturnValue(method = "getAttackDistanceScalingFactor", at = @At("RETURN"))
	private double enchancement$modifyDetectionRange(double original, Entity entity) {
		if (entity == null || (entity instanceof Monster && !entity.getType().isIn(ModEntityTypeTags.VEIL_IMMUNE))) {
			return EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.MODIFY_DETECTION_RANGE, (LivingEntity) (Object) this, (float) original);
		}
		return original;
	}
}
