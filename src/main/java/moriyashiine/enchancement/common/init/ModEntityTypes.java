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
	public static final EntityType<FrozenPlayer> FROZEN_PLAYER = registerEntityType("frozen_player", EntityType.Builder.of(FrozenPlayer::new, MobCategory.MISC).sized(EntityType.PLAYER.getWidth(), EntityType.PLAYER.getHeight()).noLootTable(), FrozenPlayer.createMobAttributes());
	public static final EntityType<AmethystShard> AMETHYST_SHARD = registerEntityType("amethyst_shard", EntityType.Builder.<AmethystShard>of(AmethystShard::new, MobCategory.MISC).sized(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()));
	public static final EntityType<Brimstone> BRIMSTONE = registerEntityType("brimstone", EntityType.Builder.<Brimstone>of(Brimstone::new, MobCategory.MISC).sized(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()).clientTrackingRange(64));
	public static final EntityType<IceShard> ICE_SHARD = registerEntityType("ice_shard", EntityType.Builder.<IceShard>of(IceShard::new, MobCategory.MISC).sized(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()));
	public static final EntityType<Torch> TORCH = registerEntityType("torch", EntityType.Builder.<Torch>of(Torch::new, MobCategory.MISC).sized(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()));

	public static void init() {
	}
}
