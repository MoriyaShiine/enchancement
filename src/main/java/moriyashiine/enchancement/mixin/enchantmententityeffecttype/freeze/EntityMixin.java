/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmententityeffecttype.freeze;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
	@Inject(method = "clearFreeze", at = @At("TAIL"))
	private void enchancement$freeze(CallbackInfo ci) {
		ModEntityComponents.FROZEN.maybeGet(this).ifPresent(frozenComponent -> frozenComponent.setFreezeTicks(0));
	}
}
