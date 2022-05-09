/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.frostbite.client.integration.geckolib;

import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.model.provider.IAnimatableModelProvider;

@Environment(EnvType.CLIENT)
@Mixin(value = {AnimatedGeoModel.class, AnimatedTickingGeoModel.class}, remap = false)
public abstract class FrozenGeoModelMixin<T extends IAnimatable> extends GeoModelProvider<T> implements IAnimatableModel<T>, IAnimatableModelProvider<T> {
	@Inject(method = "setLivingAnimations*", at = @At(value = "INVOKE", target = "Lsoftware/bernie/geckolib3/core/processor/AnimationProcessor;preAnimationSetup(Lsoftware/bernie/geckolib3/core/IAnimatable;D)V", shift = At.Shift.BY, by = -2))
	private void enchancement$frostbite(T entity, Integer uniqueID, AnimationEvent<T> customPredicate, CallbackInfo ci) {
		ModEntityComponents.FROZEN.maybeGet(entity).ifPresent(frozenComponent -> {
			if (frozenComponent.isFrozen()) {
				seekTime = frozenComponent.getForcedClientAge();
			}
		});
	}
}
