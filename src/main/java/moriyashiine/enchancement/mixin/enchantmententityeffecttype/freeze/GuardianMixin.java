/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmententityeffecttype.freeze;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.monster.Guardian;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Guardian.class)
public class GuardianMixin {
	@ModifyReturnValue(method = "getTailAnimation", at = @At("RETURN"))
	private float enchancement$freezeTailAngle(float original) {
		if (ModEntityComponents.FROZEN.get(this).isFrozen()) {
			return ModEntityComponents.FROZEN_GUARDIAN.get(this).getForcedTailAnimation();
		}
		return original;
	}

	@ModifyReturnValue(method = "getSpikesAnimation", at = @At("RETURN"))
	private float enchancement$freezeSpikesExtension(float original) {
		if (ModEntityComponents.FROZEN.get(this).isFrozen()) {
			return ModEntityComponents.FROZEN_GUARDIAN.get(this).getForcedSpikesAnimation();
		}
		return original;
	}
}
