/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.entity.mob.FrozenPlayerEntity;
import moriyashiine.enchancement.common.entity.projectile.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntityTypes {
	public static final EntityType<FrozenPlayerEntity> FROZEN_PLAYER = EntityType.Builder.create(FrozenPlayerEntity::new, SpawnGroup.MISC).dimensions(EntityType.PLAYER.getWidth(), EntityType.PLAYER.getHeight()).build();
	public static final EntityType<IceShardEntity> ICE_SHARD = EntityType.Builder.<IceShardEntity>create(IceShardEntity::new, SpawnGroup.MISC).dimensions(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()).build();
	public static final EntityType<BrimstoneEntity> BRIMSTONE = EntityType.Builder.<BrimstoneEntity>create(BrimstoneEntity::new, SpawnGroup.MISC).dimensions(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()).build();
	public static final EntityType<AmethystShardEntity> AMETHYST_SHARD = EntityType.Builder.<AmethystShardEntity>create(AmethystShardEntity::new, SpawnGroup.MISC).dimensions(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()).build();
	public static final EntityType<TorchEntity> TORCH = EntityType.Builder.<TorchEntity>create(TorchEntity::new, SpawnGroup.MISC).dimensions(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()).build();
	public static final EntityType<GrappleFishingBobberEntity> GRAPPLE_FISHING_BOBBER = EntityType.Builder.<GrappleFishingBobberEntity>create(GrappleFishingBobberEntity::new, SpawnGroup.MISC).disableSaving().disableSummon().dimensions(EntityType.FISHING_BOBBER.getWidth(), EntityType.FISHING_BOBBER.getHeight()).build();

	public static void init() {
		Registry.register(Registries.ENTITY_TYPE, Enchancement.id("frozen_player"), FROZEN_PLAYER);
		FabricDefaultAttributeRegistry.register(FROZEN_PLAYER, FrozenPlayerEntity.createMobAttributes());
		Registry.register(Registries.ENTITY_TYPE, Enchancement.id("ice_shard"), ICE_SHARD);
		Registry.register(Registries.ENTITY_TYPE, Enchancement.id("brimstone"), BRIMSTONE);
		Registry.register(Registries.ENTITY_TYPE, Enchancement.id("amethyst_shard"), AMETHYST_SHARD);
		Registry.register(Registries.ENTITY_TYPE, Enchancement.id("torch"), TORCH);
		Registry.register(Registries.ENTITY_TYPE, Enchancement.id("grapple_fishing_bobber"), GRAPPLE_FISHING_BOBBER);
	}
}
