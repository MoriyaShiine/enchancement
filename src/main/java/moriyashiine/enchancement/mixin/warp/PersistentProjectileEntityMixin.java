/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.warp;

import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin {
	@Inject(method = "onBlockHit", at = @At("TAIL"))
	private void enchancement$warp(BlockHitResult blockHitResult, CallbackInfo ci) {
		if (PersistentProjectileEntity.class.cast(this) instanceof TridentEntity entity && entity.getOwner() instanceof LivingEntity living) {
			ModEntityComponents.WARP.maybeGet(entity).ifPresent(warpComponent -> {
				if (warpComponent.hasWarp()) {
					living.getWorld().playSoundFromEntity(null, living, ModSoundEvents.ENTITY_GENERIC_TELEPORT, living.getSoundCategory(), 1, 1);
					BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());
					living.getWorld().emitGameEvent(GameEvent.TELEPORT, living.getPos(), GameEvent.Emitter.of(living, living.getSteppingBlockState()));
					living.requestTeleport(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					living.getWorld().sendEntityStatus(living, (byte) 46);
					if (living instanceof PathAwareEntity pathAware) {
						pathAware.getNavigation().stop();
					}
					warpComponent.setHasWarp(false);
				}
			});
		}
	}
}
