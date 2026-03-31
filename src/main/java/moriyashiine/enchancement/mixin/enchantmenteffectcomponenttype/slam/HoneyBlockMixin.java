/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.slam;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.HoneyBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HoneyBlock.class)
public class HoneyBlockMixin {
	@Inject(method = "doSlideMovement", at = @At("TAIL"))
	private void enchancement$slam(Entity entity, CallbackInfo ci) {
		ModEntityComponents.SLAM.maybeGet(entity).ifPresent(slamComponent -> slamComponent.setSlamming(false));
	}
}
