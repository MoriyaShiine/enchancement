/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.veil;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyReturnValue(method = "getAttackDistanceScalingFactor", at = @At("RETURN"))
	private double enchancement$veil(double original, Entity entity) {
		if (entity == null || !entity.getType().isIn(ModTags.EntityTypes.VEIL_IMMUNE)) {
			int level = EnchantmentHelper.getEquipmentLevel(ModEnchantments.VEIL, (LivingEntity) (Object) this);
			if (level > 0) {
				return original / (level * 2);
			}
		}
		return original;
	}
}
