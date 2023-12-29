/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.delay;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.item.BowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = BowItem.class, priority = 1001)
public class BowItemMixin {
	@Unique
	private static Args data = null;

	@ModifyArgs(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V"))
	private void enchancement$delay(Args args) {
		data = args;
	}

	@ModifyArg(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	private Entity enchancement$delay(Entity value) {
		ModEntityComponents.DELAY.maybeGet(value).ifPresent(delayComponent -> {
			if (delayComponent.hasDelay()) {
				delayComponent.setCachedSpeed(data.get(4));
				delayComponent.setCachedDivergence(data.get(5));
			}
			data = null;
		});
		return value;
	}
}
