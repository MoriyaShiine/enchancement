/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.registry;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.entity.mob.FrozenPlayerEntity;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import moriyashiine.enchancement.common.entity.projectile.GrappleFishingBobberEntity;
import moriyashiine.enchancement.common.entity.projectile.IceShardEntity;
import moriyashiine.enchancement.common.entity.projectile.TorchEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.registry.Registry;

public class ModEntityTypes {
	public static final EntityType<FrozenPlayerEntity> FROZEN_PLAYER = FabricEntityTypeBuilder.createLiving().entityFactory(FrozenPlayerEntity::new).defaultAttributes(MobEntity::createMobAttributes).dimensions(EntityType.PLAYER.getDimensions()).build();
	public static final EntityType<IceShardEntity> ICE_SHARD = FabricEntityTypeBuilder.<IceShardEntity>create(SpawnGroup.MISC, IceShardEntity::new).dimensions(EntityType.ARROW.getDimensions()).build();
	public static final EntityType<BrimstoneEntity> BRIMSTONE = FabricEntityTypeBuilder.<BrimstoneEntity>create(SpawnGroup.MISC, BrimstoneEntity::new).dimensions(EntityType.ARROW.getDimensions()).build();
	public static final EntityType<TorchEntity> TORCH = FabricEntityTypeBuilder.<TorchEntity>create(SpawnGroup.MISC, TorchEntity::new).dimensions(EntityType.ARROW.getDimensions()).build();
	public static final EntityType<GrappleFishingBobberEntity> GRAPPLE_FISHING_BOBBER = FabricEntityTypeBuilder.<GrappleFishingBobberEntity>create(SpawnGroup.MISC, GrappleFishingBobberEntity::new).disableSaving().disableSummon().dimensions(EntityType.FISHING_BOBBER.getDimensions()).build();

	public static void init() {
		Registry.register(Registry.ENTITY_TYPE, Enchancement.id("frozen_player"), FROZEN_PLAYER);
		Registry.register(Registry.ENTITY_TYPE, Enchancement.id("ice_shard"), ICE_SHARD);
		Registry.register(Registry.ENTITY_TYPE, Enchancement.id("brimstone"), BRIMSTONE);
		Registry.register(Registry.ENTITY_TYPE, Enchancement.id("torch"), TORCH);
		Registry.register(Registry.ENTITY_TYPE, Enchancement.id("grapple_fishing_bobber"), GRAPPLE_FISHING_BOBBER);
	}
}
