/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.teleportonhit;

import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.TeleportOnHitComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public abstract class ProjectileMixin extends Entity {
	@Shadow
	@Nullable
	public abstract Entity getOwner();

	public ProjectileMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Inject(method = "onHitBlock", at = @At("TAIL"))
	private void enchancement$teleportOnHit(BlockHitResult hitResult, CallbackInfo ci) {
		if (getOwner() instanceof LivingEntity living && level() instanceof ServerLevel level) {
			TeleportOnHitComponent teleportOnHitComponent = ModEntityComponents.TELEPORT_ON_HIT.get(this);
			if (teleportOnHitComponent.teleportsOnBlockHit()) {
				BlockPos pos = hitResult.getBlockPos().relative(hitResult.getDirection());
				teleport(living, level, new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5), teleportOnHitComponent);
			}
		}
	}

	@Inject(method = "onHitEntity", at = @At("TAIL"))
	private void enchancement$teleportOnHit(EntityHitResult hitResult, CallbackInfo ci) {
		if (getOwner() instanceof LivingEntity living && level() instanceof ServerLevel level) {
			TeleportOnHitComponent teleportOnHitComponent = ModEntityComponents.TELEPORT_ON_HIT.get(this);
			if (teleportOnHitComponent.teleportsOnEntityHit()) {
				Vec3 pos = hitResult.getLocation();
				teleport(living, level, pos.add(0, 0.5, 0), teleportOnHitComponent);
			}
		}
	}

	@Unique
	private void teleport(LivingEntity living, ServerLevel targetWorld, Vec3 targetPos, TeleportOnHitComponent teleportOnHitComponent) {
		SLibUtils.playSound(living, ModSoundEvents.ENTITY_GENERIC_TELEPORT);
		living.level().gameEvent(GameEvent.TELEPORT, living.position(), GameEvent.Context.of(living, living.getBlockStateOn()));
		living.teleport(new TeleportTransition(targetWorld, targetPos, Vec3.ZERO, living.getYHeadRot(), living.getXRot(), TeleportTransition.DO_NOTHING));
		SLibUtils.playSound(living, ModSoundEvents.ENTITY_GENERIC_TELEPORT);
		targetWorld.broadcastEntityEvent(living, EntityEvent.TELEPORT);
		if (living instanceof PathfinderMob mob) {
			mob.getNavigation().stop();
		}
		teleportOnHitComponent.disable();
	}
}
