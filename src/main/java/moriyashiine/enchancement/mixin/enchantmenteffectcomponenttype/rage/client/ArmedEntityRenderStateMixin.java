/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rage.client;

import moriyashiine.enchancement.client.render.entity.state.ArmedEntityRenderStateAddition;
import moriyashiine.enchancement.common.enchantment.effect.RageEffect;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmedEntityRenderState.class)
public class ArmedEntityRenderStateMixin implements ArmedEntityRenderStateAddition {
	@Unique
	private int leftRageColor = -1, rightRageColor = -1;

	@Override
	public int enchancement$getRageColor(Arm arm) {
		return arm == Arm.LEFT ? leftRageColor : rightRageColor;
	}

	@Override
	public void enchancement$setRageColor(Arm arm, int color) {
		if (arm == Arm.LEFT) {
			leftRageColor = color;
		} else {
			rightRageColor = color;
		}
	}

	@Inject(method = "updateRenderState", at = @At("TAIL"))
	private static void enchancement$rage(LivingEntity entity, ArmedEntityRenderState state, ItemModelManager itemModelManager, CallbackInfo ci) {
		for (Arm arm : Arm.values()) {
			((ArmedEntityRenderStateAddition) state).enchancement$setRageColor(arm, RageEffect.getColor(entity, entity.getStackInArm(arm)));
		}
	}
}
