/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.world.item.enchantment.effects.Ignite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Ignite.class)
public class IgniteMixin {
	@ModifyArg(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;igniteForSeconds(F)V"))
	private float enchancement$rebalanceEnchantments(float value) {
		if (ModConfig.rebalanceEnchantments) {
			return value * 3 / 4;
		}
		return value;
	}
}
