/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.modifydetectionrange;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.tag.ModEntityTypeTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyReturnValue(method = "getVisibilityPercent", at = @At("RETURN"))
	private double enchancement$modifyDetectionRange(double original, Entity targetingEntity) {
		if (targetingEntity == null || (targetingEntity instanceof Enemy && !targetingEntity.is(ModEntityTypeTags.VEIL_IMMUNE))) {
			return EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.MODIFY_DETECTION_RANGE, (LivingEntity) (Object) this, (float) original);
		}
		return original;
	}
}
