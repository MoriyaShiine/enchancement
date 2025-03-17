/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.enchantment.effect.*;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.enchantment.effect.TargetedEnchantmentEffect;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Unit;

import java.util.List;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerEnchantmentEffectComponentType;

public class ModEnchantmentEffectComponentTypes {
	public static final ComponentType<AirJumpEffect> AIR_JUMP = registerEnchantmentEffectComponentType("air_jump", builder -> builder.codec(AirJumpEffect.CODEC));
	public static final ComponentType<Unit> ALLOW_CROSSBOW_COOLDOWN_RELOADING = registerEnchantmentEffectComponentType("allow_crossbow_cooldown_reloading", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<Unit> ALLOW_INTERRUPTION = registerEnchantmentEffectComponentType("allow_interruption", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<AllowLoadingProjectileEffect>>> ALLOW_LOADING_PROJECTILE = registerEnchantmentEffectComponentType("allow_loading_projectile", builder -> builder.codec(EnchantmentEffectEntry.createCodec(AllowLoadingProjectileEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<List<EnchantmentEffectEntry<ApplyRandomStatusEffectEffect>>> APPLY_RANDOM_STATUS_EFFECT = registerEnchantmentEffectComponentType("apply_random_status_effect", builder -> builder.codec(EnchantmentEffectEntry.createCodec(ApplyRandomStatusEffectEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<EnchantmentValueEffect> BOOST_IN_FLUID = registerEnchantmentEffectComponentType("boost_in_fluid", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<Unit> BOUNCE = registerEnchantmentEffectComponentType("bounce", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<BrimstoneEffect> BRIMSTONE = registerEnchantmentEffectComponentType("brimstone", builder -> builder.codec(BrimstoneEffect.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<EnchantmentValueEffect>>> BURY_ENTITY = registerEnchantmentEffectComponentType("bury_entity", builder -> builder.codec(EnchantmentEffectEntry.createCodec(EnchantmentValueEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<List<EnchantmentEffectEntry<EnchantmentValueEffect>>> CHAIN_LIGHTNING = registerEnchantmentEffectComponentType("chain_lightning", builder -> builder.codec(EnchantmentEffectEntry.createCodec(EnchantmentValueEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<ChargeJumpEffect> CHARGE_JUMP = registerEnchantmentEffectComponentType("charge_jump", builder -> builder.codec(ChargeJumpEffect.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<CriticalTipperEffect>>> CRITICAL_TIPPER = registerEnchantmentEffectComponentType("critical_tipper", builder -> builder.codec(EnchantmentEffectEntry.createCodec(CriticalTipperEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<List<EnchantmentEffectEntry<DelayedLaunchEffect>>> DELAYED_LAUNCH = registerEnchantmentEffectComponentType("delayed_launch", builder -> builder.codec(EnchantmentEffectEntry.createCodec(DelayedLaunchEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<DirectionBurstEffect> DIRECTION_BURST = registerEnchantmentEffectComponentType("direction_burst", builder -> builder.codec(DirectionBurstEffect.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<DisarmingFishingBobberEffect>>> DISARMING_FISHING_BOBBER = registerEnchantmentEffectComponentType("disarming_fishing_bobber", builder -> builder.codec(EnchantmentEffectEntry.createCodec(DisarmingFishingBobberEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<EnchantmentValueEffect> ENTITY_XRAY = registerEnchantmentEffectComponentType("entity_xray", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<EruptionEffect> ERUPTION = registerEnchantmentEffectComponentType("eruption", builder -> builder.codec(EruptionEffect.CODEC));
	public static final ComponentType<Unit> EXTENDED_WATER_SPIN_ATTACK = registerEnchantmentEffectComponentType("extended_water_spin_attack", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<Unit> EXTEND_WATER_TIME = registerEnchantmentEffectComponentType("extend_water_time", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<EnchantmentValueEffect> FELL_TREES = registerEnchantmentEffectComponentType("fell_trees", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<Unit> FLUID_WALKING = registerEnchantmentEffectComponentType("fluid_walking", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<GlideEffect> GLIDE = registerEnchantmentEffectComponentType("glide", builder -> builder.codec(GlideEffect.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<EnchantmentValueEffect>>> GRAPPLING_FISHING_BOBBER = registerEnchantmentEffectComponentType("grappling_fishing_bobber", builder -> builder.codec(EnchantmentEffectEntry.createCodec(EnchantmentValueEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<List<TargetedEnchantmentEffect<EnchantmentValueEffect>>> HEAD_DROPS = registerEnchantmentEffectComponentType("head_drops", builder -> builder.codec(TargetedEnchantmentEffect.createEquipmentDropsCodec(EnchantmentValueEffect.CODEC, LootContextTypes.ENCHANTED_DAMAGE).listOf()));
	public static final ComponentType<Unit> HIDE_LABEL_BEHIND_WALLS = registerEnchantmentEffectComponentType("hide_label_behind_walls", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<EnchantmentValueEffect> HONEY_TRAIL = registerEnchantmentEffectComponentType("honey_trail", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<LeechingTridentEffect>>> LEECHING_TRIDENT = registerEnchantmentEffectComponentType("leeching_trident", builder -> builder.codec(EnchantmentEffectEntry.createCodec(LeechingTridentEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<LightningDashEffect> LIGHTNING_DASH = registerEnchantmentEffectComponentType("lightning_dash", builder -> builder.codec(LightningDashEffect.CODEC));
	public static final ComponentType<EnchantmentValueEffect> MINE_ORE_VEINS = registerEnchantmentEffectComponentType("mine_ore_veins", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<EnchantmentValueEffect> MODIFY_CONSUMPTION_TIME = registerEnchantmentEffectComponentType("modify_consumption_time", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<EnchantmentValueEffect> MODIFY_DETECTION_RANGE = registerEnchantmentEffectComponentType("modify_detection_range", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<ModifySubmergedMovementSpeedEffect> MODIFY_SUBMERGED_MOVEMENT_SPEED = registerEnchantmentEffectComponentType("modify_submerged_movement_speed", builder -> builder.codec(ModifySubmergedMovementSpeedEffect.CODEC));
	public static final ComponentType<EnchantmentValueEffect> MULTIPLY_CHARGE_TIME = registerEnchantmentEffectComponentType("multiply_charge_time", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<EnchantmentValueEffect> NIGHT_VISION = registerEnchantmentEffectComponentType("night_vision", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<EnchantmentValueEffect>>> PHASE_THROUGH_BLOCKS_AND_FLOAT = registerEnchantmentEffectComponentType("phase_through_blocks_and_float", builder -> builder.codec(EnchantmentEffectEntry.createCodec(EnchantmentValueEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<Unit> PREVENT_SWIMMING = registerEnchantmentEffectComponentType("prevent_swimming", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<RageEffect> RAGE = registerEnchantmentEffectComponentType("rage", builder -> builder.codec(RageEffect.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<EnchantmentValueEffect>>> RANGED_SHOOT_COOLDOWN = registerEnchantmentEffectComponentType("ranged_shoot_cooldown", builder -> builder.codec(EnchantmentEffectEntry.createCodec(EnchantmentValueEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<Unit> RAPID_CROSSBOW_FIRE = registerEnchantmentEffectComponentType("rapid_crossbow_fire", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<RotationBurstEffect> ROTATION_BURST = registerEnchantmentEffectComponentType("rotation_burst", builder -> builder.codec(RotationBurstEffect.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<ScatterShotEffect>>> SCATTER_SHOT = registerEnchantmentEffectComponentType("scatter_shot", builder -> builder.codec(EnchantmentEffectEntry.createCodec(ScatterShotEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<EnchantmentValueEffect> SLAM = registerEnchantmentEffectComponentType("slam", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<EnchantmentValueEffect> SLIDE = registerEnchantmentEffectComponentType("slide", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<Unit> SMELT_MINED_BLOCKS = registerEnchantmentEffectComponentType("smelt_mined_blocks", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<TeleportOnHitEffect>>> TELEPORT_ON_HIT = registerEnchantmentEffectComponentType("teleport_on_hit", builder -> builder.codec(EnchantmentEffectEntry.createCodec(TeleportOnHitEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<EnchantmentValueEffect> WALL_JUMP = registerEnchantmentEffectComponentType("wall_jump", builder -> builder.codec(EnchantmentValueEffect.CODEC));

	public static void init() {
	}
}
