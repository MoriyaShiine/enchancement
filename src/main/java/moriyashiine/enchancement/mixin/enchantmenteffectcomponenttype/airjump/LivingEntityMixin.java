/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.airjump;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.AirJumpComponent;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyReturnValue(method = "calculateFallPower", at = @At("RETURN"))
	private double enchancement$airJump(double original) {
		AirJumpComponent airJump = EnchancementEntityComponents.AIR_JUMP.getNullable(this);
		if (airJump != null && airJump.hasEffect()) {
			original -= airJump.getMaxJumps() - airJump.getJumpsLeft();
		}
		return original;
	}
}
