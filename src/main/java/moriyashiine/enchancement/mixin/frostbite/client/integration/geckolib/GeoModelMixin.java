/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.frostbite.client.integration.geckolib;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
@Mixin(value = GeoModel.class, remap = false)
public class GeoModelMixin<T extends GeoAnimatable> {
	@Shadow
	private double animTime;

	@Inject(method = "handleAnimations*", at = @At(value = "INVOKE", target = "Lsoftware/bernie/geckolib/core/animation/AnimationProcessor;preAnimationSetup(Lsoftware/bernie/geckolib/core/animatable/GeoAnimatable;D)V", shift = At.Shift.BY, by = -2))
	private void enchancement$frostbite(T animatable, long instanceId, AnimationState<T> animationEvent, CallbackInfo ci) {
		ModEntityComponents.FROZEN.maybeGet(animatable).ifPresent(frozenComponent -> {
			if (frozenComponent.isFrozen()) {
				animTime = frozenComponent.getForcedClientAge();
			}
		});
	}
}
