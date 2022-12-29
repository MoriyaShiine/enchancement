/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.frostbite.client.integration.geckolib;

import moriyashiine.enchancement.client.reloadlisteners.FrozenReloadListener;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@Environment(EnvType.CLIENT)
@Mixin(value = GeoRenderLayer.class, remap = false)
public class GeoRenderLayerMixin<T extends GeoAnimatable> {
	@Inject(method = "getTextureResource", at = @At("RETURN"), cancellable = true)
	private void enchancement$frostbite(T animatable, CallbackInfoReturnable<Identifier> cir) {
		ModEntityComponents.FROZEN.maybeGet(animatable).ifPresent(frozenComponent -> {
			if (frozenComponent.isFrozen()) {
				cir.setReturnValue(FrozenReloadListener.INSTANCE.getTexture(cir.getReturnValue()));
			}
		});
	}
}
