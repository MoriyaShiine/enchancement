/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.teleportonhit;

import moriyashiine.enchancement.common.component.entity.TeleportOnHitComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin extends Entity {
	@Shadow
	@Nullable
	public abstract Entity getOwner();

	public ProjectileEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "onBlockHit", at = @At("TAIL"))
	private void enchancement$teleportOnHit(BlockHitResult blockHitResult, CallbackInfo ci) {
		if (getOwner() instanceof LivingEntity living && getEntityWorld() instanceof ServerWorld world) {
			TeleportOnHitComponent teleportOnHitComponent = ModEntityComponents.TELEPORT_ON_HIT.get(this);
			if (teleportOnHitComponent.teleportsOnBlockHit()) {
				BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());
				teleport(living, world, new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5), teleportOnHitComponent);
			}
		}
	}

	@Inject(method = "onEntityHit", at = @At("TAIL"))
	private void enchancement$teleportOnHit(EntityHitResult entityHitResult, CallbackInfo ci) {
		if (getOwner() instanceof LivingEntity living && getEntityWorld() instanceof ServerWorld world) {
			TeleportOnHitComponent teleportOnHitComponent = ModEntityComponents.TELEPORT_ON_HIT.get(this);
			if (teleportOnHitComponent.teleportsOnEntityHit()) {
				Vec3d pos = entityHitResult.getPos();
				teleport(living, world, pos.add(0, 0.5, 0), teleportOnHitComponent);
			}
		}
	}

	@Unique
	private void teleport(LivingEntity living, ServerWorld targetWorld, Vec3d targetPos, TeleportOnHitComponent teleportOnHitComponent) {
		SLibUtils.playSound(living, ModSoundEvents.ENTITY_GENERIC_TELEPORT);
		living.getEntityWorld().emitGameEvent(GameEvent.TELEPORT, living.getEntityPos(), GameEvent.Emitter.of(living, living.getSteppingBlockState()));
		living.teleportTo(new TeleportTarget(targetWorld, targetPos, Vec3d.ZERO, living.getHeadYaw(), living.getPitch(), TeleportTarget.NO_OP));
		SLibUtils.playSound(living, ModSoundEvents.ENTITY_GENERIC_TELEPORT);
		targetWorld.sendEntityStatus(living, EntityStatuses.ADD_PORTAL_PARTICLES);
		if (living instanceof PathAwareEntity pathAware) {
			pathAware.getNavigation().stop();
		}
		teleportOnHitComponent.disable();
	}
}
