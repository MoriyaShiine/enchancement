/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.entity.mob.FrozenPlayerEntity;
import moriyashiine.enchancement.common.entity.projectile.AmethystShardEntity;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import moriyashiine.enchancement.common.entity.projectile.IceShardEntity;
import moriyashiine.enchancement.common.entity.projectile.TorchEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntityTypes {
	public static final EntityType<FrozenPlayerEntity> FROZEN_PLAYER = register("frozen_player", EntityType.Builder.create(FrozenPlayerEntity::new, SpawnGroup.MISC).dimensions(EntityType.PLAYER.getWidth(), EntityType.PLAYER.getHeight()).dropsNothing());
	public static final EntityType<AmethystShardEntity> AMETHYST_SHARD = register("amethyst_shard", EntityType.Builder.<AmethystShardEntity>create(AmethystShardEntity::new, SpawnGroup.MISC).dimensions(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()));
	public static final EntityType<BrimstoneEntity> BRIMSTONE = register("brimstone", EntityType.Builder.<BrimstoneEntity>create(BrimstoneEntity::new, SpawnGroup.MISC).dimensions(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()).maxTrackingRange(64));
	public static final EntityType<IceShardEntity> ICE_SHARD = register("ice_shard", EntityType.Builder.<IceShardEntity>create(IceShardEntity::new, SpawnGroup.MISC).dimensions(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()));
	public static final EntityType<TorchEntity> TORCH = register("torch", EntityType.Builder.<TorchEntity>create(TorchEntity::new, SpawnGroup.MISC).dimensions(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()));

	@SuppressWarnings("unchecked")
	private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
		Identifier id = Enchancement.id(name);
		EntityType<?> type = builder.build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, id));
		return (EntityType<T>) Registry.register(Registries.ENTITY_TYPE, id, type);
	}

	public static void init() {
		FabricDefaultAttributeRegistry.register(FROZEN_PLAYER, FrozenPlayerEntity.createMobAttributes());
	}
}
