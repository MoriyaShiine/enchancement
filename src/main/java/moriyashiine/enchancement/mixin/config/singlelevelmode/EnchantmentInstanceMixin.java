/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.singlelevelmode;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EnchantmentInstance.class)
public class EnchantmentInstanceMixin {
	@ModifyVariable(method = "<init>", at = @At(value = "HEAD"), argsOnly = true)
	private static int enchancement$singleLevelMode(int level) {
		if (ModConfig.singleLevelMode) {
			return 1;
		}
		return level;
	}
}
