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
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.Identifier;

public class ModEntityComponents implements EntityComponentInitializer {
	//chestplate
	public static final ComponentKey<StrafeComponent> STRAFE = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "strafe"), StrafeComponent.class);
	//leggings
	public static final ComponentKey<JumpingComponent> JUMPING = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "jumping"), JumpingComponent.class);
	public static final ComponentKey<DashComponent> DASH = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "dash"), DashComponent.class);
	public static final ComponentKey<ImpactComponent> IMPACT = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "impact"), ImpactComponent.class);
	public static final ComponentKey<SlideComponent> SLIDE = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "slide"), SlideComponent.class);
	//boots
	public static final ComponentKey<MovingForwardComponent> MOVING_FORWARD = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "moving_forward"), MovingForwardComponent.class);
	public static final ComponentKey<AccelerationComponent> ACCELERATION = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "acceleration"), AccelerationComponent.class);
	public static final ComponentKey<GaleComponent> GALE = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "gale"), GaleComponent.class);
	//sword
	public static final ComponentKey<BerserkComponent> BERSERK = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "berserk"), BerserkComponent.class);
	public static final ComponentKey<FrozenComponent> FROZEN = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "frozen"), FrozenComponent.class);
	//bow
	public static final ComponentKey<DelayComponent> DELAY = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "delay"), DelayComponent.class);
	public static final ComponentKey<PhasingComponent> PHASHING = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "phasing"), PhasingComponent.class);
	//trident
	public static final ComponentKey<ChannelingComponent> CHANNELING = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "channeling"), ChannelingComponent.class);
	public static final ComponentKey<LeechComponent> LEECH = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "leech"), LeechComponent.class);
	public static final ComponentKey<WarpComponent> WARP = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "warp"), WarpComponent.class);
	//shovel
	public static final ComponentKey<BuryComponent> BURY = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "bury"), BuryComponent.class);
	//fishing rod
	public static final ComponentKey<WitchDisarmComponent> WITCH_DISARM = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "witch_disarm"), WitchDisarmComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(STRAFE, StrafeComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(JUMPING, JumpingComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(DASH, DashComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(IMPACT, ImpactComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(SLIDE, SlideComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(MOVING_FORWARD, MovingForwardComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(ACCELERATION, AccelerationComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(GALE, GaleComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerForPlayers(BERSERK, player -> new BerserkComponent(), RespawnCopyStrategy.LOSSLESS_ONLY);
		registry.registerFor(LivingEntity.class, FROZEN, FrozenComponent::new);
		registry.registerFor(ArrowEntity.class, DELAY, DelayComponent::new);
		registry.registerFor(ArrowEntity.class, PHASHING, PhasingComponent::new);
		registry.registerFor(LightningEntity.class, CHANNELING, lightning -> new ChannelingComponent());
		registry.registerFor(TridentEntity.class, LEECH, LeechComponent::new);
		registry.registerFor(TridentEntity.class, WARP, WarpComponent::new);
		registry.registerFor(LivingEntity.class, BURY, BuryComponent::new);
		registry.registerFor(WitchEntity.class, WITCH_DISARM, witch -> new WitchDisarmComponent());
	}
}
