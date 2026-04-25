/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.*;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ModEntityComponents implements EntityComponentInitializer {
	public static final ComponentKey<AirJumpComponent> AIR_JUMP = ComponentRegistry.getOrCreate(Enchancement.id("air_jump"), AirJumpComponent.class);
	public static final ComponentKey<AirMobilityComponent> AIR_MOBILITY = ComponentRegistry.getOrCreate(Enchancement.id("air_mobility"), AirMobilityComponent.class);
	public static final ComponentKey<ApplyRandomMobEffectComponent> APPLY_RANDOM_MOB_EFFECT = ComponentRegistry.getOrCreate(Enchancement.id("apply_random_mob_effect"), ApplyRandomMobEffectComponent.class);
	public static final ComponentKey<ApplyRandomMobEffectGenericComponent> APPLY_RANDOM_MOB_EFFECT_GENERIC = ComponentRegistry.getOrCreate(Enchancement.id("apply_random_mob_effect_generic"), ApplyRandomMobEffectGenericComponent.class);
	public static final ComponentKey<BoostInFluidComponent> BOOST_IN_FLUID = ComponentRegistry.getOrCreate(Enchancement.id("boost_in_fluid"), BoostInFluidComponent.class);
	public static final ComponentKey<BounceComponent> BOUNCE = ComponentRegistry.getOrCreate(Enchancement.id("bounce"), BounceComponent.class);
	public static final ComponentKey<BuryEntityComponent> BURY_ENTITY = ComponentRegistry.getOrCreate(Enchancement.id("bury_entity"), BuryEntityComponent.class);
	public static final ComponentKey<ChargeJumpComponent> CHARGE_JUMP = ComponentRegistry.getOrCreate(Enchancement.id("charge_jump"), ChargeJumpComponent.class);
	public static final ComponentKey<ConditionalAttributesComponent> CONDITIONAL_ATTRIBUTES = ComponentRegistry.getOrCreate(Enchancement.id("conditional_attributes"), ConditionalAttributesComponent.class);
	public static final ComponentKey<DelayedLaunchComponent> DELAYED_LAUNCH = ComponentRegistry.getOrCreate(Enchancement.id("delayed_launch"), DelayedLaunchComponent.class);
	public static final ComponentKey<DirectionBurstComponent> DIRECTION_BURST = ComponentRegistry.getOrCreate(Enchancement.id("direction_burst"), DirectionBurstComponent.class);
	public static final ComponentKey<DisarmingFishingBobberComponent> DISARMING_FISHING_BOBBER = ComponentRegistry.getOrCreate(Enchancement.id("disarming_fishing_bobber"), DisarmingFishingBobberComponent.class);
	public static final ComponentKey<DisarmedPlayerComponent> DISARMED_PLAYER = ComponentRegistry.getOrCreate(Enchancement.id("disarmed_player"), DisarmedPlayerComponent.class);
	public static final ComponentKey<DisarmedWanderingTraderComponent> DISARMED_WANDERING_TRADER = ComponentRegistry.getOrCreate(Enchancement.id("disarmed_wandering_trader"), DisarmedWanderingTraderComponent.class);
	public static final ComponentKey<DisarmedWitchComponent> DISARMED_WITCH = ComponentRegistry.getOrCreate(Enchancement.id("disarmed_witch"), DisarmedWitchComponent.class);
	public static final ComponentKey<UsingMaceComponent> ERUPTION = ComponentRegistry.getOrCreate(Enchancement.id("eruption"), UsingMaceComponent.class);
	public static final ComponentKey<ExtendedWaterTimeComponent> EXTENDED_WATER_TIME = ComponentRegistry.getOrCreate(Enchancement.id("extended_water_time"), ExtendedWaterTimeComponent.class);
	public static final ComponentKey<FrozenComponent> FROZEN = ComponentRegistry.getOrCreate(Enchancement.id("frozen"), FrozenComponent.class);
	public static final ComponentKey<FrozenGuardianComponent> FROZEN_GUARDIAN = ComponentRegistry.getOrCreate(Enchancement.id("frozen_guardian"), FrozenGuardianComponent.class);
	public static final ComponentKey<FrozenSquidComponent> FROZEN_SQUID = ComponentRegistry.getOrCreate(Enchancement.id("frozen_squid"), FrozenSquidComponent.class);
	public static final ComponentKey<GlideComponent> GLIDE = ComponentRegistry.getOrCreate(Enchancement.id("glide"), GlideComponent.class);
	public static final ComponentKey<GroundedCooldownComponent> GROUNDED_COOLDOWN = ComponentRegistry.getOrCreate(Enchancement.id("grounded_cooldown"), GroundedCooldownComponent.class);
	public static final ComponentKey<IgniteKnockbackComponent> IGNITE_KNOCKBACK = ComponentRegistry.getOrCreate(Enchancement.id("ignite_knockback"), IgniteKnockbackComponent.class);
	public static final ComponentKey<InCombatComponent> IN_COMBAT = ComponentRegistry.getOrCreate(Enchancement.id("in_combat"), InCombatComponent.class);
	public static final ComponentKey<UsingMaceComponent> LAUNCH_WIND_CHARGE = ComponentRegistry.getOrCreate(Enchancement.id("launch_wind_charge"), UsingMaceComponent.class);
	public static final ComponentKey<LeechingTridentComponent> LEECHING_TRIDENT = ComponentRegistry.getOrCreate(Enchancement.id("leeching_trident"), LeechingTridentComponent.class);
	public static final ComponentKey<LightningDashComponent> LIGHTNING_DASH = ComponentRegistry.getOrCreate(Enchancement.id("lightning_dash"), LightningDashComponent.class);
	public static final ComponentKey<OwnedTridentComponent> OWNED_TRIDENT = ComponentRegistry.getOrCreate(Enchancement.id("owned_trident"), OwnedTridentComponent.class);
	public static final ComponentKey<PhaseThroughBlocksAndFloatComponent> PHASE_THROUGH_BLOCKS_AND_FLOAT = ComponentRegistry.getOrCreate(Enchancement.id("phase_through_blocks_and_float"), PhaseThroughBlocksAndFloatComponent.class);
	public static final ComponentKey<ProjectileTimerComponent> PROJECTILE_TIMER = ComponentRegistry.getOrCreate(Enchancement.id("projectile_timer"), ProjectileTimerComponent.class);
	public static final ComponentKey<RotationBurstComponent> ROTATION_BURST = ComponentRegistry.getOrCreate(Enchancement.id("rotation_burst"), RotationBurstComponent.class);
	public static final ComponentKey<SafeLightningComponent> SAFE_LIGHTNING = ComponentRegistry.getOrCreate(Enchancement.id("safe_lightning"), SafeLightningComponent.class);
	public static final ComponentKey<SlamComponent> SLAM = ComponentRegistry.getOrCreate(Enchancement.id("slam"), SlamComponent.class);
	public static final ComponentKey<SlideComponent> SLIDE = ComponentRegistry.getOrCreate(Enchancement.id("slide"), SlideComponent.class);
	public static final ComponentKey<TeleportOnHitComponent> TELEPORT_ON_HIT = ComponentRegistry.getOrCreate(Enchancement.id("teleport_on_hit"), TeleportOnHitComponent.class);
	public static final ComponentKey<WallJumpComponent> WALL_JUMP = ComponentRegistry.getOrCreate(Enchancement.id("wall_jump"), WallJumpComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(AIR_JUMP, AirJumpComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(LivingEntity.class, AIR_MOBILITY, AirMobilityComponent::new);
		registry.registerFor(Arrow.class, APPLY_RANDOM_MOB_EFFECT, _ -> new ApplyRandomMobEffectComponent());
		registry.registerFor(AbstractArrow.class, APPLY_RANDOM_MOB_EFFECT_GENERIC, ApplyRandomMobEffectGenericComponent::new);
		registry.registerFor(LivingEntity.class, BOOST_IN_FLUID, BoostInFluidComponent::new);
		registry.beginRegistration(LivingEntity.class, BOUNCE).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(BounceComponent::new);
		registry.registerFor(LivingEntity.class, BURY_ENTITY, BuryEntityComponent::new);
		registry.registerForPlayers(CHARGE_JUMP, ChargeJumpComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(LivingEntity.class, CONDITIONAL_ATTRIBUTES, ConditionalAttributesComponent::new);
		registry.registerFor(AbstractArrow.class, DELAYED_LAUNCH, DelayedLaunchComponent::new);
		registry.registerForPlayers(DIRECTION_BURST, DirectionBurstComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(FishingHook.class, DISARMING_FISHING_BOBBER, _ -> new DisarmingFishingBobberComponent());
		registry.registerForPlayers(DISARMED_PLAYER, DisarmedPlayerComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(WanderingTrader.class, DISARMED_WANDERING_TRADER, _ -> new DisarmedWanderingTraderComponent());
		registry.registerFor(Witch.class, DISARMED_WITCH, _ -> new DisarmedWitchComponent());
		registry.registerForPlayers(ERUPTION, _ -> new UsingMaceComponent(), RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(LivingEntity.class, EXTENDED_WATER_TIME, ExtendedWaterTimeComponent::new);
		registry.registerFor(LivingEntity.class, FROZEN, FrozenComponent::new);
		registry.registerFor(Guardian.class, FROZEN_GUARDIAN, FrozenGuardianComponent::new);
		registry.registerFor(Squid.class, FROZEN_SQUID, FrozenSquidComponent::new);
		registry.registerFor(LivingEntity.class, GLIDE, GlideComponent::new);
		registry.registerForPlayers(GROUNDED_COOLDOWN, GroundedCooldownComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(LivingEntity.class, IGNITE_KNOCKBACK, IgniteKnockbackComponent::new);
		registry.registerFor(LivingEntity.class, IN_COMBAT, _ -> new InCombatComponent());
		registry.registerForPlayers(LAUNCH_WIND_CHARGE, _ -> new UsingMaceComponent(), RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(ThrownTrident.class, LEECHING_TRIDENT, LeechingTridentComponent::new);
		registry.registerForPlayers(LIGHTNING_DASH, LightningDashComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(ThrownTrident.class, OWNED_TRIDENT, OwnedTridentComponent::new);
		registry.registerFor(AbstractArrow.class, PHASE_THROUGH_BLOCKS_AND_FLOAT, PhaseThroughBlocksAndFloatComponent::new);
		registry.registerFor(LivingEntity.class, PROJECTILE_TIMER, _ -> new ProjectileTimerComponent());
		registry.registerForPlayers(ROTATION_BURST, RotationBurstComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(LightningBolt.class, SAFE_LIGHTNING, _ -> new SafeLightningComponent());
		registry.registerForPlayers(SLAM, SlamComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(SLIDE, SlideComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(Projectile.class, TELEPORT_ON_HIT, TeleportOnHitComponent::new);
		registry.registerFor(LivingEntity.class, WALL_JUMP, WallJumpComponent::new);
	}
}
