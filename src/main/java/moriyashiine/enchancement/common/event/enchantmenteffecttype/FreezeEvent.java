/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.event.enchantmenteffecttype;

import moriyashiine.enchancement.client.payload.SyncFrozenPlayerSlimStatusS2CPayload;
import moriyashiine.enchancement.common.component.entity.enchantmenteffecttype.FrozenComponent;
import moriyashiine.enchancement.common.component.entity.enchantmenteffecttype.FrozenGuardianComponent;
import moriyashiine.enchancement.common.component.entity.enchantmenteffecttype.FrozenSquidComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModEntityTypes;
import moriyashiine.enchancement.common.world.entity.decoration.FrozenPlayer;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.IceShard;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class FreezeEvent {
	public static class HandleDeath implements ServerLivingEntityEvents.AfterDeath {
		@Override
		public void afterDeath(LivingEntity entity, DamageSource damageSource) {
			FrozenComponent frozenComponent = ModEntityComponents.FROZEN.get(entity);
			if (frozenComponent.shouldFreezeOnDeath(damageSource)) {
				if (entity instanceof ServerPlayer player) {
					FrozenPlayer frozenPlayer = ModEntityTypes.FROZEN_PLAYER.create(entity.level(), EntitySpawnReason.TRIGGERED);
					if (frozenPlayer != null) {
						frozenPlayer.setCustomName(entity.getName());
						frozenPlayer.setPersistenceRequired();
						frozenPlayer.setYHeadRot(entity.getYHeadRot());
						frozenPlayer.setYBodyRot(entity.getVisualRotationYInDegrees());
						frozenPlayer.setXRot(entity.getXRot());
						frozenPlayer.tickCount = entity.tickCount;
						frozenPlayer.teleportTo(entity.getX(), entity.getY(), entity.getZ());
						ModEntityComponents.FROZEN.get(frozenPlayer).freeze();
						SyncFrozenPlayerSlimStatusS2CPayload.send(player, frozenPlayer.getUUID());
						entity.level().addFreshEntity(frozenPlayer);
					}
				} else {
					if (entity instanceof Guardian guardian) {
						FrozenGuardianComponent frozenGuardianComponent = ModEntityComponents.FROZEN_GUARDIAN.get(guardian);
						frozenGuardianComponent.setForcedTailAnimation(guardian.getTailAnimation(1));
						frozenGuardianComponent.setForcedSpikesAnimation(guardian.getRandom().nextFloat());
						frozenGuardianComponent.sync();
						guardian.setActiveAttackTarget(0);
					} else if (entity instanceof Squid squid) {
						FrozenSquidComponent frozenSquidComponent = ModEntityComponents.FROZEN_SQUID.get(squid);
						frozenSquidComponent.setForcedTentacleAngle(squid.tentacleAngle);
						frozenSquidComponent.setForcedXBodyRot(squid.xBodyRot);
						frozenSquidComponent.setForcedZBodyRot(squid.zBodyRot);
						frozenSquidComponent.sync();
					}
					frozenComponent.freeze();
				}
			}
		}
	}

	public static class HandleDamage implements ServerLivingEntityEvents.AllowDamage {
		@Override
		public boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
			FrozenComponent frozenComponent = ModEntityComponents.FROZEN.get(entity);
			if (FrozenComponent.isSourceFrostbiteWeapon(source)) {
				frozenComponent.setLastFreezingAttacker(source.getEntity());
			}
			if (frozenComponent.isFrozen()) {
				if (source.is(DamageTypes.FREEZE)) {
					return false;
				} else {
					Entity entitySource = source.getDirectEntity();
					if (entitySource != null && amount <= 1) {
						entity.setDeltaMovement(entity.getDeltaMovement().add(-(entitySource.getX() - entity.getX()), 0, -(entitySource.getZ() - entity.getZ())).normalize().scale(0.5));
						return false;
					} else {
						for (int i = 0; i < 4; i++) {
							if (entity.level().getEntities(ModEntityTypes.ICE_SHARD, new AABB(entity.blockPosition()).inflate(2), foundEntity -> true).size() < 64) {
								for (int j = 0; j < Mth.nextInt(entity.getRandom(), 6, 8); j++) {
									IceShard iceShard = new IceShard(entity.level(), entity, frozenComponent.getLastFreezingAttacker());
									Vec3 random = new Vec3(entity.getRandom().nextGaussian(), entity.getRandom().nextGaussian() / 2, entity.getRandom().nextGaussian());
									iceShard.shoot(random.x(), random.y(), random.z(), 0.75F, 0);
									entity.level().addFreshEntity(iceShard);
								}
							}
						}
						entity.level().gameEvent(GameEvent.ENTITY_DIE, entity.position(), GameEvent.Context.of(entity, entity.getBlockStateOn()));
						entity.discard();
					}
				}
			}
			return true;
		}
	}
}
