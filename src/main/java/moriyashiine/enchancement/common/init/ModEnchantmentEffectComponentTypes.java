/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.enchantment.effect.*;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.enchantment.effect.TargetedEnchantmentEffect;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Unit;

import java.util.List;
import java.util.function.UnaryOperator;

public class ModEnchantmentEffectComponentTypes {
	public static final ComponentType<AirJumpEffect> AIR_JUMP = register("air_jump", builder -> builder.codec(AirJumpEffect.CODEC));
	public static final ComponentType<Unit> ALLOW_CROSSBOW_COOLDOWN_RELOADING = register("allow_crossbow_cooldown_reloading", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<Unit> ALLOW_INTERRUPTION = register("allow_interruption", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<AllowLoadingProjectileEffect>>> ALLOW_LOADING_PROJECTILE = register("allow_loading_projectile", builder -> builder.codec(EnchantmentEffectEntry.createCodec(AllowLoadingProjectileEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<List<EnchantmentEffectEntry<ApplyRandomStatusEffectEffect>>> APPLY_RANDOM_STATUS_EFFECT = register("apply_random_status_effect", builder -> builder.codec(EnchantmentEffectEntry.createCodec(ApplyRandomStatusEffectEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<EnchantmentValueEffect> BOOST_IN_FLUID = register("boost_in_fluid", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<Unit> BOUNCE = register("bounce", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<BrimstoneEffect> BRIMSTONE = register("brimstone", builder -> builder.codec(BrimstoneEffect.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<EnchantmentValueEffect>>> BURY_ENTITY = register("bury_entity", builder -> builder.codec(EnchantmentEffectEntry.createCodec(EnchantmentValueEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<List<EnchantmentEffectEntry<EnchantmentValueEffect>>> CHAIN_LIGHTNING = register("chain_lightning", builder -> builder.codec(EnchantmentEffectEntry.createCodec(EnchantmentValueEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<ChargeJumpEffect> CHARGE_JUMP = register("charge_jump", builder -> builder.codec(ChargeJumpEffect.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<DelayedLaunchEffect>>> DELAYED_LAUNCH = register("delayed_launch", builder -> builder.codec(EnchantmentEffectEntry.createCodec(DelayedLaunchEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<DirectionBurstEffect> DIRECTION_BURST = register("direction_burst", builder -> builder.codec(DirectionBurstEffect.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<DisarmingFishingBobberEffect>>> DISARMING_FISHING_BOBBER = register("disarming_fishing_bobber", builder -> builder.codec(EnchantmentEffectEntry.createCodec(DisarmingFishingBobberEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<EnchantmentValueEffect> ENTITY_XRAY = register("entity_xray", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<EruptionEffect> ERUPTION = register("eruption", builder -> builder.codec(EruptionEffect.CODEC));
	public static final ComponentType<Unit> EXTENDED_WATER_SPIN_ATTACK = register("extended_water_spin_attack", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<Unit> EXTEND_WATER_TIME = register("extend_water_time", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<EnchantmentValueEffect> FELL_TREES = register("fell_trees", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<Unit> FLUID_WALKING = register("fluid_walking", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<EnchantmentValueEffect>>> GRAPPLING_FISHING_BOBBER = register("grappling_fishing_bobber", builder -> builder.codec(EnchantmentEffectEntry.createCodec(EnchantmentValueEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<List<TargetedEnchantmentEffect<EnchantmentValueEffect>>> HEAD_DROPS = register("head_drops", builder -> builder.codec(TargetedEnchantmentEffect.createEquipmentDropsCodec(EnchantmentValueEffect.CODEC, LootContextTypes.ENCHANTED_DAMAGE).listOf()));
	public static final ComponentType<Unit> HIDE_LABEL_BEHIND_WALLS = register("hide_label_behind_walls", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<Unit> HONEY_TRAIL = register("honey_trail", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<LeechingTridentEffect>>> LEECHING_TRIDENT = register("leeching_trident", builder -> builder.codec(EnchantmentEffectEntry.createCodec(LeechingTridentEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<LightningDashEffect> LIGHTNING_DASH = register("lightning_dash", builder -> builder.codec(LightningDashEffect.CODEC));
	public static final ComponentType<EnchantmentValueEffect> MINE_ORE_VEINS = register("mine_ore_veins", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<EnchantmentValueEffect> MODIFY_CONSUMPTION_TIME = register("modify_consumption_time", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<EnchantmentValueEffect> MODIFY_DETECTION_RANGE = register("modify_detection_range", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<ModifySubmergedMovementSpeedEffect> MODIFY_SUBMERGED_MOVEMENT_SPEED = register("modify_submerged_movement_speed", builder -> builder.codec(ModifySubmergedMovementSpeedEffect.CODEC));
	public static final ComponentType<EnchantmentValueEffect> MULTIPLY_CHARGE_TIME = register("multiply_charge_time", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<EnchantmentValueEffect> NIGHT_VISION = register("night_vision", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<EnchantmentValueEffect>>> PHASE_THROUGH_BLOCKS_AND_FLOAT = register("phase_through_blocks_and_float", builder -> builder.codec(EnchantmentEffectEntry.createCodec(EnchantmentValueEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<Unit> PREVENT_SWIMMING = register("prevent_swimming", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<RageEffect> RAGE = register("rage", builder -> builder.codec(RageEffect.CODEC));
	public static final ComponentType<Unit> RAPID_CROSSBOW_FIRE = register("rapid_crossbow_fire", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<RotationBurstEffect> ROTATION_BURST = register("rotation_burst", builder -> builder.codec(RotationBurstEffect.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<ScatterShotEffect>>> SCATTER_SHOT = register("scatter_shot", builder -> builder.codec(EnchantmentEffectEntry.createCodec(ScatterShotEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<EnchantmentValueEffect> SLAM = register("slam", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<EnchantmentValueEffect> SLIDE = register("slide", builder -> builder.codec(EnchantmentValueEffect.CODEC));
	public static final ComponentType<Unit> SMELT_MINED_BLOCKS = register("smelt_mined_blocks", builder -> builder.codec(Unit.CODEC));
	public static final ComponentType<List<EnchantmentEffectEntry<TeleportOnHitEffect>>> TELEPORT_ON_HIT = register("teleport_on_hit", builder -> builder.codec(EnchantmentEffectEntry.createCodec(TeleportOnHitEffect.CODEC, LootContextTypes.ENCHANTED_ITEM).listOf()));
	public static final ComponentType<EnchantmentValueEffect> WALL_JUMP = register("wall_jump", builder -> builder.codec(EnchantmentValueEffect.CODEC));

	private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
		return Registry.register(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, Enchancement.id(id), builderOperator.apply(ComponentType.builder()).build());
	}

	public static void init() {
	}
}
