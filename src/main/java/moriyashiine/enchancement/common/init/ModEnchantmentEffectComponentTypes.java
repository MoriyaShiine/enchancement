/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.world.item.effects.*;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.util.Unit;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.TargetedConditionalEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerEnchantmentEffectComponentType;
import static net.minecraft.world.item.enchantment.EnchantmentEffectComponents.validatedListCodec;

public class ModEnchantmentEffectComponentTypes {
	public static final DataComponentType<AirJumpEffect> AIR_JUMP = registerEnchantmentEffectComponentType("air_jump", builder -> builder.persistent(AirJumpEffect.CODEC));
	public static final DataComponentType<Unit> ALLOW_CROSSBOW_COOLDOWN_RELOADING = registerEnchantmentEffectComponentType("allow_crossbow_cooldown_reloading", builder -> builder.persistent(Unit.CODEC));
	public static final DataComponentType<Unit> ALLOW_INTERRUPTION = registerEnchantmentEffectComponentType("allow_interruption", builder -> builder.persistent(Unit.CODEC));
	public static final DataComponentType<List<ConditionalEffect<AllowLoadingProjectileEffect>>> ALLOW_LOADING_PROJECTILE = registerEnchantmentEffectComponentType("allow_loading_projectile", builder -> builder.persistent(validatedListCodec(ConditionalEffect.codec(AllowLoadingProjectileEffect.CODEC), LootContextParamSets.ENCHANTED_ITEM)));
	public static final DataComponentType<List<ConditionalEffect<ApplyRandomMobEffectEffect>>> APPLY_RANDOM_MOB_EFFECT = registerEnchantmentEffectComponentType("apply_random_mob_effect", builder -> builder.persistent(validatedListCodec(ConditionalEffect.codec(ApplyRandomMobEffectEffect.CODEC), LootContextParamSets.ENCHANTED_ITEM)));
	public static final DataComponentType<EnchantmentValueEffect> BOOST_IN_FLUID = registerEnchantmentEffectComponentType("boost_in_fluid", builder -> builder.persistent(EnchantmentValueEffect.CODEC));
	public static final DataComponentType<Unit> BOUNCE = registerEnchantmentEffectComponentType("bounce", builder -> builder.persistent(Unit.CODEC));
	public static final DataComponentType<BrimstoneEffect> BRIMSTONE = registerEnchantmentEffectComponentType("brimstone", builder -> builder.persistent(BrimstoneEffect.CODEC));
	public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> BURY_ENTITY = registerEnchantmentEffectComponentType("bury_entity", builder -> builder.persistent(validatedListCodec(ConditionalEffect.codec(EnchantmentValueEffect.CODEC), LootContextParamSets.ENCHANTED_ITEM)));
	public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> CHAIN_LIGHTNING = registerEnchantmentEffectComponentType("chain_lightning", builder -> builder.persistent(validatedListCodec(ConditionalEffect.codec(EnchantmentValueEffect.CODEC), LootContextParamSets.ENCHANTED_ITEM)));
	public static final DataComponentType<ChargeJumpEffect> CHARGE_JUMP = registerEnchantmentEffectComponentType("charge_jump", builder -> builder.persistent(ChargeJumpEffect.CODEC));
	public static final DataComponentType<List<ConditionalEffect<CriticalTipperEffect>>> CRITICAL_TIPPER = registerEnchantmentEffectComponentType("critical_tipper", builder -> builder.persistent(validatedListCodec(ConditionalEffect.codec(CriticalTipperEffect.CODEC), LootContextParamSets.ENCHANTED_ITEM)));
	public static final DataComponentType<List<ConditionalEffect<DelayedLaunchEffect>>> DELAYED_LAUNCH = registerEnchantmentEffectComponentType("delayed_launch", builder -> builder.persistent(validatedListCodec(ConditionalEffect.codec(DelayedLaunchEffect.CODEC), LootContextParamSets.ENCHANTED_ITEM)));
	public static final DataComponentType<DirectionBurstEffect> DIRECTION_BURST = registerEnchantmentEffectComponentType("direction_burst", builder -> builder.persistent(DirectionBurstEffect.CODEC));
	public static final DataComponentType<List<ConditionalEffect<DisarmingFishingBobberEffect>>> DISARMING_FISHING_BOBBER = registerEnchantmentEffectComponentType("disarming_fishing_bobber", builder -> builder.persistent(validatedListCodec(ConditionalEffect.codec(DisarmingFishingBobberEffect.CODEC), LootContextParamSets.ENCHANTED_ITEM)));
	public static final DataComponentType<EnchantmentValueEffect> ENTITY_XRAY = registerEnchantmentEffectComponentType("entity_xray", builder -> builder.persistent(EnchantmentValueEffect.CODEC));
	public static final DataComponentType<EruptionEffect> ERUPTION = registerEnchantmentEffectComponentType("eruption", builder -> builder.persistent(EruptionEffect.CODEC));
	public static final DataComponentType<Unit> EXTENDED_WATER_SPIN_ATTACK = registerEnchantmentEffectComponentType("extended_water_spin_attack", builder -> builder.persistent(Unit.CODEC));
	public static final DataComponentType<Unit> EXTEND_WATER_TIME = registerEnchantmentEffectComponentType("extend_water_time", builder -> builder.persistent(Unit.CODEC));
	public static final DataComponentType<EnchantmentValueEffect> FELL_TREES = registerEnchantmentEffectComponentType("fell_trees", builder -> builder.persistent(EnchantmentValueEffect.CODEC));
	public static final DataComponentType<Unit> FLUID_WALKING = registerEnchantmentEffectComponentType("fluid_walking", builder -> builder.persistent(Unit.CODEC));
	public static final DataComponentType<GlideEffect> GLIDE = registerEnchantmentEffectComponentType("glide", builder -> builder.persistent(GlideEffect.CODEC));
	public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> GRAPPLING_FISHING_BOBBER = registerEnchantmentEffectComponentType("grappling_fishing_bobber", builder -> builder.persistent(validatedListCodec(ConditionalEffect.codec(EnchantmentValueEffect.CODEC), LootContextParamSets.ENCHANTED_ITEM)));
	public static final DataComponentType<List<TargetedConditionalEffect<EnchantmentValueEffect>>> HEAD_DROPS = registerEnchantmentEffectComponentType("head_drops", builder -> builder.persistent(validatedListCodec(TargetedConditionalEffect.equipmentDropsCodec(EnchantmentValueEffect.CODEC), LootContextParamSets.ENCHANTED_DAMAGE)));
	public static final DataComponentType<Unit> HIDE_NAME_BEHIND_WALLS = registerEnchantmentEffectComponentType("hide_name_behind_walls", builder -> builder.persistent(Unit.CODEC));
	public static final DataComponentType<Unit> HIDE_NON_ARMOR_ATTRIBUTE_TOOLTIPS = registerEnchantmentEffectComponentType("hide_non_armor_attribute_tooltips", builder -> builder.persistent(Unit.CODEC));
	public static final DataComponentType<EnchantmentValueEffect> HONEY_TRAIL = registerEnchantmentEffectComponentType("honey_trail", builder -> builder.persistent(EnchantmentValueEffect.CODEC));
	public static final DataComponentType<List<ConditionalEffect<LeechingTridentEffect>>> LEECHING_TRIDENT = registerEnchantmentEffectComponentType("leeching_trident", builder -> builder.persistent(validatedListCodec(ConditionalEffect.codec(LeechingTridentEffect.CODEC), LootContextParamSets.ENCHANTED_ITEM)));
	public static final DataComponentType<LightningDashEffect> LIGHTNING_DASH = registerEnchantmentEffectComponentType("lightning_dash", builder -> builder.persistent(LightningDashEffect.CODEC));
	public static final DataComponentType<EnchantmentValueEffect> MINE_ORE_VEINS = registerEnchantmentEffectComponentType("mine_ore_veins", builder -> builder.persistent(EnchantmentValueEffect.CODEC));
	public static final DataComponentType<EnchantmentValueEffect> MODIFY_CONSUMPTION_TIME = registerEnchantmentEffectComponentType("modify_consumption_time", builder -> builder.persistent(EnchantmentValueEffect.CODEC));
	public static final DataComponentType<EnchantmentValueEffect> MODIFY_DETECTION_RANGE = registerEnchantmentEffectComponentType("modify_detection_range", builder -> builder.persistent(EnchantmentValueEffect.CODEC));
	public static final DataComponentType<ModifySubmergedMovementSpeedEffect> MODIFY_SUBMERGED_MOVEMENT_SPEED = registerEnchantmentEffectComponentType("modify_submerged_movement_speed", builder -> builder.persistent(ModifySubmergedMovementSpeedEffect.CODEC));
	public static final DataComponentType<EnchantmentValueEffect> NIGHT_VISION = registerEnchantmentEffectComponentType("night_vision", builder -> builder.persistent(EnchantmentValueEffect.CODEC));
	public static final DataComponentType<List<ConditionalEffect<PhaseEffect>>> PHASE = registerEnchantmentEffectComponentType("phase", builder -> builder.persistent(validatedListCodec(ConditionalEffect.codec(PhaseEffect.CODEC), LootContextParamSets.ENCHANTED_ITEM)));
	public static final DataComponentType<Unit> PREVENT_SWIMMING = registerEnchantmentEffectComponentType("prevent_swimming", builder -> builder.persistent(Unit.CODEC));
	public static final DataComponentType<RageEffect> RAGE = registerEnchantmentEffectComponentType("rage", builder -> builder.persistent(RageEffect.CODEC));
	public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> RANGED_SHOOT_COOLDOWN = registerEnchantmentEffectComponentType("ranged_shoot_cooldown", builder -> builder.persistent(validatedListCodec(ConditionalEffect.codec(EnchantmentValueEffect.CODEC), LootContextParamSets.ENCHANTED_ITEM)));
	public static final DataComponentType<Unit> RAPID_CROSSBOW_FIRE = registerEnchantmentEffectComponentType("rapid_crossbow_fire", builder -> builder.persistent(Unit.CODEC));
	public static final DataComponentType<RotationBurstEffect> ROTATION_BURST = registerEnchantmentEffectComponentType("rotation_burst", builder -> builder.persistent(RotationBurstEffect.CODEC));
	public static final DataComponentType<List<ConditionalEffect<ScatterShotEffect>>> SCATTER_SHOT = registerEnchantmentEffectComponentType("scatter_shot", builder -> builder.persistent(validatedListCodec(ConditionalEffect.codec(ScatterShotEffect.CODEC), LootContextParamSets.ENCHANTED_ITEM)));
	public static final DataComponentType<EnchantmentValueEffect> SLAM = registerEnchantmentEffectComponentType("slam", builder -> builder.persistent(EnchantmentValueEffect.CODEC));
	public static final DataComponentType<EnchantmentValueEffect> SLIDE = registerEnchantmentEffectComponentType("slide", builder -> builder.persistent(EnchantmentValueEffect.CODEC));
	public static final DataComponentType<Unit> SMELT_MINED_BLOCKS = registerEnchantmentEffectComponentType("smelt_mined_blocks", builder -> builder.persistent(Unit.CODEC));
	public static final DataComponentType<List<ConditionalEffect<TeleportOnHitEffect>>> TELEPORT_ON_HIT = registerEnchantmentEffectComponentType("teleport_on_hit", builder -> builder.persistent(validatedListCodec(ConditionalEffect.codec(TeleportOnHitEffect.CODEC), LootContextParamSets.ENCHANTED_ITEM)));
	public static final DataComponentType<EnchantmentValueEffect> WALL_JUMP = registerEnchantmentEffectComponentType("wall_jump", builder -> builder.persistent(EnchantmentValueEffect.CODEC));

	public static void init() {
	}
}
