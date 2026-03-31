/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.fluidwalking;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {
	@ModifyReturnValue(method = "canEntityWalkOnPowderSnow", at = @At("RETURN"))
	private static boolean enchancement$fluidWalking(boolean original, Entity entity) {
		return original || EnchancementUtil.hasAnyEnchantmentsWith(entity, ModEnchantmentEffectComponentTypes.FLUID_WALKING);
	}
}
