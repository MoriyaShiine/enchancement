/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.teleportonhit;

import moriyashiine.enchancement.common.component.entity.TeleportOnHitComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin {
	@Shadow
	@Nullable
	public abstract Entity getOwner();

	@Inject(method = "onBlockHit", at = @At("TAIL"))
	private void enchancement$teleportOnHit(BlockHitResult blockHitResult, CallbackInfo ci) {
		if (getOwner() instanceof LivingEntity living) {
			TeleportOnHitComponent teleportOnHitComponent = ModEntityComponents.TELEPORT_ON_HIT.get(this);
			if (teleportOnHitComponent.teleportsOnBlockHit()) {
				living.getWorld().playSoundFromEntity(null, living, ModSoundEvents.ENTITY_GENERIC_TELEPORT, living.getSoundCategory(), 1, 1);
				BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());
				living.getWorld().emitGameEvent(GameEvent.TELEPORT, living.getPos(), GameEvent.Emitter.of(living, living.getSteppingBlockState()));
				living.requestTeleport(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
				living.getWorld().sendEntityStatus(living, (byte) 46);
				if (living instanceof PathAwareEntity pathAware) {
					pathAware.getNavigation().stop();
				}
				teleportOnHitComponent.disable();
			}
		}
	}

	@Inject(method = "onEntityHit", at = @At("TAIL"))
	private void enchancement$teleportOnHit(EntityHitResult entityHitResult, CallbackInfo ci) {
		if (getOwner() instanceof LivingEntity living) {
			TeleportOnHitComponent teleportOnHitComponent = ModEntityComponents.TELEPORT_ON_HIT.get(this);
			if (teleportOnHitComponent.teleportsOnEntityHit()) {
				living.getWorld().playSoundFromEntity(null, living, ModSoundEvents.ENTITY_GENERIC_TELEPORT, living.getSoundCategory(), 1, 1);
				Vec3d pos = entityHitResult.getPos();
				living.getWorld().emitGameEvent(GameEvent.TELEPORT, living.getPos(), GameEvent.Emitter.of(living, living.getSteppingBlockState()));
				living.requestTeleport(pos.getX(), pos.getY() + 0.5, pos.getZ());
				living.getWorld().sendEntityStatus(living, (byte) 46);
				if (living instanceof PathAwareEntity pathAware) {
					pathAware.getNavigation().stop();
				}
				teleportOnHitComponent.disable();
			}
		}
	}
}
