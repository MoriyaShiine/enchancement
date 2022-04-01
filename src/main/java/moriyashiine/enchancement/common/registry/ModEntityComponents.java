package moriyashiine.enchancement.common.registry;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.Identifier;

public class ModEntityComponents implements EntityComponentInitializer {
	//helmet
	public static final ComponentKey<AssimilationComponent> ASSIMILATION = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "assimilation"), AssimilationComponent.class);
	public static final ComponentKey<BuffetComponent> BUFFET = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "buffet"), BuffetComponent.class);
	//leggings
	public static final ComponentKey<DashComponent> DASH = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "dash"), DashComponent.class);
	public static final ComponentKey<SlideComponent> SLIDE = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "slide"), SlideComponent.class);
	//boots
	public static final ComponentKey<AccelerationComponent> ACCELERATION = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "acceleration"), AccelerationComponent.class);
	public static final ComponentKey<MovingForwardComponent> MOVING_FORWARD = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "moving_forward"), MovingForwardComponent.class);
	public static final ComponentKey<GaleComponent> GALE = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "gale"), GaleComponent.class);
	//sword
	public static final ComponentKey<FrozenComponent> FROZEN = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "frozen"), FrozenComponent.class);
	//bow
	public static final ComponentKey<DelayComponent> DELAY = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "delay"), DelayComponent.class);
	//trident
	public static final ComponentKey<WarpComponent> WARP = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "warp"), WarpComponent.class);
	//shovel
	public static final ComponentKey<BuryComponent> BURY = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "bury"), BuryComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(ASSIMILATION, AssimilationComponent::new, RespawnCopyStrategy.NEVER_COPY);
		registry.registerForPlayers(BUFFET, BuffetComponent::new, RespawnCopyStrategy.NEVER_COPY);
		registry.registerForPlayers(DASH, DashComponent::new, RespawnCopyStrategy.NEVER_COPY);
		registry.registerForPlayers(SLIDE, SlideComponent::new, RespawnCopyStrategy.NEVER_COPY);
		registry.registerForPlayers(ACCELERATION, AccelerationComponent::new, RespawnCopyStrategy.NEVER_COPY);
		registry.registerForPlayers(MOVING_FORWARD, MovingForwardComponent::new, RespawnCopyStrategy.NEVER_COPY);
		registry.registerForPlayers(GALE, GaleComponent::new, RespawnCopyStrategy.NEVER_COPY);
		registry.registerFor(MobEntity.class, FROZEN, FrozenComponent::new);
		registry.registerFor(ArrowEntity.class, DELAY, DelayComponent::new);
		registry.registerFor(TridentEntity.class, WARP, WarpComponent::new);
		registry.registerFor(LivingEntity.class, BURY, BuryComponent::new);
	}
}
