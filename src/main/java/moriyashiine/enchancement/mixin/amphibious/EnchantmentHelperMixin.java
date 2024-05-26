/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.amphibious;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@ModifyReturnValue(method = "getDepthStrider", at = @At("RETURN"))
	private static int enchancement$amphibiousDepthStrider(int original, LivingEntity entity) {
		int level = EnchantmentHelper.getEquipmentLevel(ModEnchantments.AMPHIBIOUS, entity);
		if (level > 0) {
			return MathHelper.ceil(EnchancementUtil.getOriginalMaxLevel(Enchantments.DEPTH_STRIDER) / 2F * level);
		}
		return original;
	}

	@ModifyReturnValue(method = "getRespiration", at = @At("RETURN"))
	private static int enchancement$amphibiousRespiration(int original, LivingEntity entity) {
		int level = EnchantmentHelper.getEquipmentLevel(ModEnchantments.AMPHIBIOUS, entity);
		if (level > 0) {
			return MathHelper.ceil(EnchancementUtil.getOriginalMaxLevel(Enchantments.RESPIRATION) / 2F * level);
		}
		return original;
	}

	@ModifyReturnValue(method = "hasAquaAffinity", at = @At("RETURN"))
	private static boolean enchancement$amphibious(boolean original, LivingEntity entity) {
		return original || EnchancementUtil.hasEnchantment(ModEnchantments.AMPHIBIOUS, entity);
	}
}
