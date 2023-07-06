/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.frostbite;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileEntity.class)
public class ProjectileEntityMixin {
	@Inject(method = "canHit", at = @At("HEAD"), cancellable = true)
	private void enchancement$frostbite(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		ModEntityComponents.FROZEN.maybeGet(entity).ifPresent(frozenComponent -> {
			if (frozenComponent.isFrozen()) {
				cir.setReturnValue(true);
			}
		});
	}
}
