/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmententityeffecttype.freeze;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.mob.GuardianEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuardianEntity.class)
public class GuardianEntityMixin {
	@ModifyReturnValue(method = "getTailAngle", at = @At("RETURN"))
	private float enchancement$freezeTailAngle(float original) {
		if (ModEntityComponents.FROZEN.get(this).isFrozen()) {
			return ModEntityComponents.FROZEN_GUARDIAN.get(this).getForcedTailAngle();
		}
		return original;
	}

	@ModifyReturnValue(method = "getSpikesExtension", at = @At("RETURN"))
	private float enchancement$freezeSpikesExtension(float original) {
		if (ModEntityComponents.FROZEN.get(this).isFrozen()) {
			return ModEntityComponents.FROZEN_GUARDIAN.get(this).getForcedSpikesExtension();
		}
		return original;
	}
}
