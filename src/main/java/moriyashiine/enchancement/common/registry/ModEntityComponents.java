/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.registry;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.*;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;

public class ModEntityComponents implements EntityComponentInitializer {
	//chestplate
	public static final ComponentKey<StrafeComponent> STRAFE = ComponentRegistry.getOrCreate(Enchancement.id("strafe"), StrafeComponent.class);
	//leggings
	public static final ComponentKey<DashComponent> DASH = ComponentRegistry.getOrCreate(Enchancement.id("dash"), DashComponent.class);
	public static final ComponentKey<SlideComponent> SLIDE = ComponentRegistry.getOrCreate(Enchancement.id("slide"), SlideComponent.class);
	//boots
	public static final ComponentKey<BouncyComponent> BOUNCY = ComponentRegistry.getOrCreate(Enchancement.id("bouncy"), BouncyComponent.class);
	public static final ComponentKey<BuoyComponent> BUOY = ComponentRegistry.getOrCreate(Enchancement.id("buoy"), BuoyComponent.class);
	public static final ComponentKey<GaleComponent> GALE = ComponentRegistry.getOrCreate(Enchancement.id("gale"), GaleComponent.class);
	//sword
	public static final ComponentKey<FrozenComponent> FROZEN = ComponentRegistry.getOrCreate(Enchancement.id("frozen"), FrozenComponent.class);
	public static final ComponentKey<FrozenSquidComponent> FROZEN_SQUID = ComponentRegistry.getOrCreate(Enchancement.id("frozen_squid"), FrozenSquidComponent.class);
	//bow
	public static final ComponentKey<DelayComponent> DELAY = ComponentRegistry.getOrCreate(Enchancement.id("delay"), DelayComponent.class);
	public static final ComponentKey<PhasingComponent> PHASHING = ComponentRegistry.getOrCreate(Enchancement.id("phasing"), PhasingComponent.class);
	//trident
	public static final ComponentKey<ChannelingComponent> CHANNELING = ComponentRegistry.getOrCreate(Enchancement.id("channeling"), ChannelingComponent.class);
	public static final ComponentKey<LeechComponent> LEECH = ComponentRegistry.getOrCreate(Enchancement.id("leech"), LeechComponent.class);
	public static final ComponentKey<WarpComponent> WARP = ComponentRegistry.getOrCreate(Enchancement.id("warp"), WarpComponent.class);
	//shovel
	public static final ComponentKey<BuryComponent> BURY = ComponentRegistry.getOrCreate(Enchancement.id("bury"), BuryComponent.class);
	//fishing rod
	public static final ComponentKey<DisarmComponent> DISARM = ComponentRegistry.getOrCreate(Enchancement.id("disarm"), DisarmComponent.class);
	public static final ComponentKey<DisarmedPlayerComponent> DISARMED_PLAYER = ComponentRegistry.getOrCreate(Enchancement.id("disarmed_player"), DisarmedPlayerComponent.class);
	public static final ComponentKey<DisarmedWitchComponent> DISARMED_WITCH = ComponentRegistry.getOrCreate(Enchancement.id("disarmed_witch"), DisarmedWitchComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(STRAFE, StrafeComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(DASH, DashComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(SLIDE, SlideComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(BOUNCY, BouncyComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(BUOY, BuoyComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(GALE, GaleComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(LivingEntity.class, FROZEN, FrozenComponent::new);
		registry.registerFor(SquidEntity.class, FROZEN_SQUID, FrozenSquidComponent::new);
		registry.registerFor(ArrowEntity.class, DELAY, DelayComponent::new);
		registry.registerFor(ArrowEntity.class, PHASHING, PhasingComponent::new);
		registry.registerFor(SpectralArrowEntity.class, PHASHING, PhasingComponent::new);
		registry.registerFor(LightningEntity.class, CHANNELING, lightning -> new ChannelingComponent());
		registry.registerFor(TridentEntity.class, LEECH, LeechComponent::new);
		registry.registerFor(TridentEntity.class, WARP, WarpComponent::new);
		registry.registerFor(LivingEntity.class, BURY, BuryComponent::new);
		registry.registerFor(FishingBobberEntity.class, DISARM, fishingBobber -> new DisarmComponent());
		registry.registerForPlayers(DISARMED_PLAYER, DisarmedPlayerComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(WitchEntity.class, DISARMED_WITCH, witch -> new DisarmedWitchComponent());
	}
}
