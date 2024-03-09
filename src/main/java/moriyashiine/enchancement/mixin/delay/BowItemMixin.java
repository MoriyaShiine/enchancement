/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.delay;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BowItem.class, priority = 1001)
public class BowItemMixin {
	@WrapOperation(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V"))
	private void enchancement$delay(PersistentProjectileEntity instance, Entity shooter, float pitch, float yaw, float roll, float speed, float divergence, Operation<Void> original) {
		original.call(instance, shooter, pitch, yaw, roll, speed, divergence);
		ModEntityComponents.DELAY.maybeGet(instance).ifPresent(delayComponent -> {
			if (delayComponent.hasDelay()) {
				delayComponent.setCachedSpeed(speed);
				delayComponent.setCachedDivergence(divergence);
			}
		});
	}
}
