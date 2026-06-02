/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.world.entity.decoration.FrozenPlayer;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.AmethystShard;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.Brimstone;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.IceShard;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.Torch;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerEntityType;

public class ModEntityTypes {
	public static final EntityType<FrozenPlayer> FROZEN_PLAYER = registerEntityType("frozen_player", EntityType.Builder.of(FrozenPlayer::new, MobCategory.MISC).noLootTable().sized(0.6F, 1.8F), FrozenPlayer.createMobAttributes());
	public static final EntityType<AmethystShard> AMETHYST_SHARD = registerEntityType("amethyst_shard", EntityType.Builder.<AmethystShard>of(AmethystShard::new, MobCategory.MISC).noLootTable().sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20));
	public static final EntityType<Brimstone> BRIMSTONE = registerEntityType("brimstone", EntityType.Builder.<Brimstone>of(Brimstone::new, MobCategory.MISC).noLootTable().sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(64).updateInterval(20));
	public static final EntityType<IceShard> ICE_SHARD = registerEntityType("ice_shard", EntityType.Builder.<IceShard>of(IceShard::new, MobCategory.MISC).noLootTable().sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20));
	public static final EntityType<Torch> TORCH = registerEntityType("torch", EntityType.Builder.<Torch>of(Torch::new, MobCategory.MISC).noLootTable().sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20));

	public static void init() {
	}
}
