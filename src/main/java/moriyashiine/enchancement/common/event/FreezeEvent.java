/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.event;

import moriyashiine.enchancement.client.payload.SyncFrozenPlayerSlimStatusS2CPayload;
import moriyashiine.enchancement.common.component.entity.FrozenComponent;
import moriyashiine.enchancement.common.component.entity.FrozenSquidComponent;
import moriyashiine.enchancement.common.entity.mob.FrozenPlayerEntity;
import moriyashiine.enchancement.common.entity.projectile.IceShardEntity;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModEntityTypes;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;

public class FreezeEvent {
	public static class HandleDeath implements ServerLivingEntityEvents.AfterDeath {
		@Override
		public void afterDeath(LivingEntity entity, DamageSource damageSource) {
			FrozenComponent frozenComponent = ModEntityComponents.FROZEN.get(entity);
			if (frozenComponent.shouldFreezeOnDeath(damageSource)) {
				if (entity instanceof ServerPlayerEntity serverPlayer) {
					FrozenPlayerEntity frozenPlayer = ModEntityTypes.FROZEN_PLAYER.create(entity.getWorld());
					if (frozenPlayer != null) {
						frozenPlayer.setCustomName(entity.getName());
						frozenPlayer.setPersistent();
						frozenPlayer.setHeadYaw(entity.getHeadYaw());
						frozenPlayer.setBodyYaw(entity.getBodyYaw());
						frozenPlayer.setPitch(entity.getPitch());
						frozenPlayer.age = entity.age;
						frozenPlayer.refreshPositionAfterTeleport(entity.getX(), entity.getY(), entity.getZ());
						ModEntityComponents.FROZEN.get(frozenPlayer).freeze();
						SyncFrozenPlayerSlimStatusS2CPayload.send(serverPlayer, frozenPlayer.getUuid());
						entity.getWorld().spawnEntity(frozenPlayer);
					}
				} else {
					frozenComponent.freeze();
					if (entity instanceof SquidEntity squid) {
						FrozenSquidComponent frozenSquidComponent = ModEntityComponents.FROZEN_SQUID.get(squid);
						frozenSquidComponent.setForcedRollAngle(squid.rollAngle);
						frozenSquidComponent.setForcedTentacleAngle(squid.tentacleAngle);
						frozenSquidComponent.setForcedTiltAngle(squid.tiltAngle);
						frozenSquidComponent.sync();
					}
				}
			}
		}
	}

	public static class HandleDamage implements ServerLivingEntityEvents.AllowDamage {
		@Override
		public boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
			FrozenComponent frozenComponent = ModEntityComponents.FROZEN.get(entity);
			if (FrozenComponent.isSourceFrostbiteWeapon(source)) {
				frozenComponent.setLastFreezingAttacker(source.getAttacker());
			}
			if (frozenComponent.isFrozen()) {
				if (source.isOf(DamageTypes.FREEZE)) {
					return false;
				} else {
					Entity entitySource = source.getSource();
					if (entitySource != null && amount <= 1) {
						entity.setVelocity(entity.getVelocity().add(-(entitySource.getX() - entity.getX()), 0, -(entitySource.getZ() - entity.getZ())).normalize().multiply(0.5));
						return false;
					} else {
						for (int i = 0; i < 4; i++) {
							if (entity.getWorld().getEntitiesByType(ModEntityTypes.ICE_SHARD, new Box(entity.getBlockPos()).expand(2), foundEntity -> true).size() < 64) {
								for (int j = 0; j < MathHelper.nextInt(entity.getRandom(), 6, 8); j++) {
									IceShardEntity iceShard = new IceShardEntity(entity.getWorld(), entity);
									iceShard.setOwner(frozenComponent.getLastFreezingAttacker());
									iceShard.refreshPositionAfterTeleport(entity.getX(), entity.getEyeY(), entity.getZ());
									iceShard.setVelocity(new Vec3d(entity.getRandom().nextGaussian(), entity.getRandom().nextGaussian() / 2, entity.getRandom().nextGaussian()).normalize().multiply(0.75));
									entity.getWorld().spawnEntity(iceShard);
								}
							}
						}
						entity.getWorld().emitGameEvent(GameEvent.ENTITY_DIE, entity.getPos(), GameEvent.Emitter.of(entity, entity.getSteppingBlockState()));
						entity.discard();
					}
				}
			}
			return true;
		}
	}
}
