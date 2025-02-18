/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.*;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.projectile.*;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ModEntityComponents implements EntityComponentInitializer {
	// misc
	public static final ComponentKey<AirMobilityComponent> AIR_MOBILITY = ComponentRegistry.getOrCreate(Enchancement.id("air_mobility"), AirMobilityComponent.class);
	public static final ComponentKey<ProjectileTimerComponent> PROJECTILE_TIMER = ComponentRegistry.getOrCreate(Enchancement.id("projectile_timer"), ProjectileTimerComponent.class);
	public static final ComponentKey<SafeLightningComponent> SAFE_LIGHTNING = ComponentRegistry.getOrCreate(Enchancement.id("safe_lightning"), SafeLightningComponent.class);
	// enchantment component
	public static final ComponentKey<AirJumpComponent> AIR_JUMP = ComponentRegistry.getOrCreate(Enchancement.id("air_jump"), AirJumpComponent.class);
	public static final ComponentKey<BoostInFluidComponent> BOOST_IN_FLUID = ComponentRegistry.getOrCreate(Enchancement.id("boost_in_fluid"), BoostInFluidComponent.class);
	public static final ComponentKey<BounceComponent> BOUNCE = ComponentRegistry.getOrCreate(Enchancement.id("bounce"), BounceComponent.class);
	public static final ComponentKey<BuryEntityComponent> BURY_ENTITY = ComponentRegistry.getOrCreate(Enchancement.id("bury_entity"), BuryEntityComponent.class);
	public static final ComponentKey<ChargeJumpComponent> CHARGE_JUMP = ComponentRegistry.getOrCreate(Enchancement.id("charge_jump"), ChargeJumpComponent.class);
	public static final ComponentKey<ConditionalAttributesComponent> CONDITIONAL_ATTRIBUTES = ComponentRegistry.getOrCreate(Enchancement.id("conditional_attributes"), ConditionalAttributesComponent.class);
	public static final ComponentKey<DirectionBurstComponent> DIRECTION_BURST = ComponentRegistry.getOrCreate(Enchancement.id("direction_burst"), DirectionBurstComponent.class);
	public static final ComponentKey<EruptionComponent> ERUPTION = ComponentRegistry.getOrCreate(Enchancement.id("eruption"), EruptionComponent.class);
	public static final ComponentKey<ExtendedWaterTimeComponent> EXTENDED_WATER_TIME = ComponentRegistry.getOrCreate(Enchancement.id("extended_water_time"), ExtendedWaterTimeComponent.class);
	public static final ComponentKey<GlideComponent> GLIDE = ComponentRegistry.getOrCreate(Enchancement.id("glide"), GlideComponent.class);
	public static final ComponentKey<LightningDashComponent> LIGHTNING_DASH = ComponentRegistry.getOrCreate(Enchancement.id("lightning_dash"), LightningDashComponent.class);
	public static final ComponentKey<RotationBurstComponent> ROTATION_BURST = ComponentRegistry.getOrCreate(Enchancement.id("rotation_burst"), RotationBurstComponent.class);
	public static final ComponentKey<SlamComponent> SLAM = ComponentRegistry.getOrCreate(Enchancement.id("slam"), SlamComponent.class);
	public static final ComponentKey<SlideComponent> SLIDE = ComponentRegistry.getOrCreate(Enchancement.id("slide"), SlideComponent.class);
	public static final ComponentKey<WallJumpComponent> WALL_JUMP = ComponentRegistry.getOrCreate(Enchancement.id("wall_jump"), WallJumpComponent.class);
	// extra entity data
	public static final ComponentKey<ApplyRandomStatusEffectComponent> APPLY_RANDOM_STATUS_EFFECT = ComponentRegistry.getOrCreate(Enchancement.id("apply_random_status_effect"), ApplyRandomStatusEffectComponent.class);
	public static final ComponentKey<ApplyRandomStatusEffectGenericComponent> APPLY_RANDOM_STATUS_EFFECT_GENERIC = ComponentRegistry.getOrCreate(Enchancement.id("apply_random_status_effect_generic"), ApplyRandomStatusEffectGenericComponent.class);
	public static final ComponentKey<DelayedLaunchComponent> DELAYED_LAUNCH = ComponentRegistry.getOrCreate(Enchancement.id("delayed_launch"), DelayedLaunchComponent.class);
	public static final ComponentKey<DisarmingFishingBobberComponent> DISARMING_FISHING_BOBBER = ComponentRegistry.getOrCreate(Enchancement.id("disarming_fishing_bobber"), DisarmingFishingBobberComponent.class);
	public static final ComponentKey<DisarmedPlayerComponent> DISARMED_PLAYER = ComponentRegistry.getOrCreate(Enchancement.id("disarmed_player"), DisarmedPlayerComponent.class);
	public static final ComponentKey<DisarmedWitchComponent> DISARMED_WITCH = ComponentRegistry.getOrCreate(Enchancement.id("disarmed_witch"), DisarmedWitchComponent.class);
	public static final ComponentKey<FrozenComponent> FROZEN = ComponentRegistry.getOrCreate(Enchancement.id("frozen"), FrozenComponent.class);
	public static final ComponentKey<FrozenGuardianComponent> FROZEN_GUARDIAN = ComponentRegistry.getOrCreate(Enchancement.id("frozen_guardian"), FrozenGuardianComponent.class);
	public static final ComponentKey<FrozenSquidComponent> FROZEN_SQUID = ComponentRegistry.getOrCreate(Enchancement.id("frozen_squid"), FrozenSquidComponent.class);
	public static final ComponentKey<InCombatComponent> IN_COMBAT = ComponentRegistry.getOrCreate(Enchancement.id("in_combat"), InCombatComponent.class);
	public static final ComponentKey<LeechingTridentComponent> LEECHING_TRIDENT = ComponentRegistry.getOrCreate(Enchancement.id("leeching_trident"), LeechingTridentComponent.class);
	public static final ComponentKey<PhaseThroughBlocksAndFloatComponent> PHASE_THROUGH_BLOCKS_AND_FLOAT = ComponentRegistry.getOrCreate(Enchancement.id("phase_through_blocks_and_float"), PhaseThroughBlocksAndFloatComponent.class);
	public static final ComponentKey<TeleportOnHitComponent> TELEPORT_ON_HIT = ComponentRegistry.getOrCreate(Enchancement.id("teleport_on_hit"), TeleportOnHitComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		// misc
		registry.registerFor(LivingEntity.class, AIR_MOBILITY, AirMobilityComponent::new);
		registry.registerFor(LivingEntity.class, PROJECTILE_TIMER, living -> new ProjectileTimerComponent());
		registry.registerFor(LightningEntity.class, SAFE_LIGHTNING, lightning -> new SafeLightningComponent());
		// enchantment component
		registry.registerForPlayers(AIR_JUMP, AirJumpComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(LivingEntity.class, BOOST_IN_FLUID, BoostInFluidComponent::new);
		registry.beginRegistration(LivingEntity.class, BOUNCE).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(BounceComponent::new);
		registry.registerFor(LivingEntity.class, BURY_ENTITY, BuryEntityComponent::new);
		registry.registerForPlayers(CHARGE_JUMP, ChargeJumpComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(LivingEntity.class, CONDITIONAL_ATTRIBUTES, ConditionalAttributesComponent::new);
		registry.registerForPlayers(DIRECTION_BURST, DirectionBurstComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(LivingEntity.class, ERUPTION, EruptionComponent::new);
		registry.registerFor(LivingEntity.class, EXTENDED_WATER_TIME, ExtendedWaterTimeComponent::new);
		registry.registerFor(LivingEntity.class, GLIDE, GlideComponent::new);
		registry.registerFor(LivingEntity.class, LIGHTNING_DASH, LightningDashComponent::new);
		registry.registerForPlayers(ROTATION_BURST, RotationBurstComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(SLAM, SlamComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(SLIDE, SlideComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(LivingEntity.class, WALL_JUMP, WallJumpComponent::new);
		// extra entity data
		registry.registerFor(ArrowEntity.class, APPLY_RANDOM_STATUS_EFFECT, projectile -> new ApplyRandomStatusEffectComponent());
		registry.registerFor(PersistentProjectileEntity.class, APPLY_RANDOM_STATUS_EFFECT_GENERIC, ApplyRandomStatusEffectGenericComponent::new);
		registry.registerFor(ArrowEntity.class, DELAYED_LAUNCH, DelayedLaunchComponent::new);
		registry.registerFor(SpectralArrowEntity.class, DELAYED_LAUNCH, DelayedLaunchComponent::new);
		registry.registerFor(FishingBobberEntity.class, DISARMING_FISHING_BOBBER, fishingBobber -> new DisarmingFishingBobberComponent());
		registry.registerForPlayers(DISARMED_PLAYER, DisarmedPlayerComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(WitchEntity.class, DISARMED_WITCH, witch -> new DisarmedWitchComponent());
		registry.registerFor(LivingEntity.class, FROZEN, FrozenComponent::new);
		registry.registerFor(GuardianEntity.class, FROZEN_GUARDIAN, FrozenGuardianComponent::new);
		registry.registerFor(SquidEntity.class, FROZEN_SQUID, FrozenSquidComponent::new);
		registry.registerFor(LivingEntity.class, IN_COMBAT, InCombatComponent::new);
		registry.registerFor(TridentEntity.class, LEECHING_TRIDENT, LeechingTridentComponent::new);
		registry.registerFor(PersistentProjectileEntity.class, PHASE_THROUGH_BLOCKS_AND_FLOAT, PhaseThroughBlocksAndFloatComponent::new);
		registry.registerFor(ProjectileEntity.class, TELEPORT_ON_HIT, TeleportOnHitComponent::new);
	}
}
