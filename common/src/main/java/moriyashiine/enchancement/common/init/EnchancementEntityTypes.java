package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.references.EnchancementEntityTypeIds;
import moriyashiine.enchancement.common.world.entity.decoration.FrozenPlayer;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.AmethystShard;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.Brimstone;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.IceShard;
import moriyashiine.enchancement.common.world.entity.projectile.arrow.Torch;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerEntityType;

public class EnchancementEntityTypes {
	public static final EntityType<FrozenPlayer> FROZEN_PLAYER = registerEntityType(EnchancementEntityTypeIds.FROZEN_PLAYER, EntityType.Builder.of(FrozenPlayer::new, MobCategory.MISC).noLootTable().sized(0.6F, 1.8F), FrozenPlayer.createMobAttributes());
	public static final EntityType<AmethystShard> AMETHYST_SHARD = registerEntityType(EnchancementEntityTypeIds.AMETHYST_SHARD, EntityType.Builder.<AmethystShard>of(AmethystShard::new, MobCategory.MISC).noLootTable().sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20));
	public static final EntityType<Brimstone> BRIMSTONE = registerEntityType(EnchancementEntityTypeIds.BRIMSTONE, EntityType.Builder.<Brimstone>of(Brimstone::new, MobCategory.MISC).noLootTable().sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(64).updateInterval(20));
	public static final EntityType<IceShard> ICE_SHARD = registerEntityType(EnchancementEntityTypeIds.ICE_SHARD, EntityType.Builder.<IceShard>of(IceShard::new, MobCategory.MISC).noLootTable().sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20));
	public static final EntityType<Torch> TORCH = registerEntityType(EnchancementEntityTypeIds.TORCH, EntityType.Builder.<Torch>of(Torch::new, MobCategory.MISC).noLootTable().sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20));

	public static void init() {
	}
}
