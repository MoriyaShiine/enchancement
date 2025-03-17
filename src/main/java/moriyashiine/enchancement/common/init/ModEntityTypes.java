/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.entity.mob.FrozenPlayerEntity;
import moriyashiine.enchancement.common.entity.projectile.AmethystShardEntity;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import moriyashiine.enchancement.common.entity.projectile.IceShardEntity;
import moriyashiine.enchancement.common.entity.projectile.TorchEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerEntityType;

public class ModEntityTypes {
	public static final EntityType<FrozenPlayerEntity> FROZEN_PLAYER = registerEntityType("frozen_player", EntityType.Builder.create(FrozenPlayerEntity::new, SpawnGroup.MISC).dimensions(EntityType.PLAYER.getWidth(), EntityType.PLAYER.getHeight()).dropsNothing(), FrozenPlayerEntity.createMobAttributes());
	public static final EntityType<AmethystShardEntity> AMETHYST_SHARD = registerEntityType("amethyst_shard", EntityType.Builder.<AmethystShardEntity>create(AmethystShardEntity::new, SpawnGroup.MISC).dimensions(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()));
	public static final EntityType<BrimstoneEntity> BRIMSTONE = registerEntityType("brimstone", EntityType.Builder.<BrimstoneEntity>create(BrimstoneEntity::new, SpawnGroup.MISC).dimensions(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()).maxTrackingRange(64));
	public static final EntityType<IceShardEntity> ICE_SHARD = registerEntityType("ice_shard", EntityType.Builder.<IceShardEntity>create(IceShardEntity::new, SpawnGroup.MISC).dimensions(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()));
	public static final EntityType<TorchEntity> TORCH = registerEntityType("torch", EntityType.Builder.<TorchEntity>create(TorchEntity::new, SpawnGroup.MISC).dimensions(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()));

	public static void init() {
	}
}
