/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.chargejump;

import moriyashiine.enchancement.common.world.item.effects.ChargeJumpEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AbstractHorse.class)
public class AbstractHorseMixin {
	@ModifyVariable(method = "executeRidersJump", at = @At("HEAD"), argsOnly = true)
	private float enchancement$chargeJump(float amount) {
		return amount + amount * ChargeJumpEffect.getStrength((LivingEntity) (Object) this, 0);
	}
}
