package moriyashiine.enchancement.common.registry;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.AccelerationComponent;
import moriyashiine.enchancement.common.component.entity.FrozenComponent;
import moriyashiine.enchancement.common.component.entity.MovingForwardComponent;
import moriyashiine.enchancement.common.component.entity.WarpTridentComponent;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.Identifier;

public class ModComponents implements EntityComponentInitializer {
	public static final ComponentKey<MovingForwardComponent> MOVING_FORWARD = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "moving_forward"), MovingForwardComponent.class);

	public static final ComponentKey<AccelerationComponent> ACCELERATION = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "acceleration"), AccelerationComponent.class);

	public static final ComponentKey<FrozenComponent> FROZEN = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "frozen"), FrozenComponent.class);

	public static final ComponentKey<WarpTridentComponent> WARP_TRIDENT = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "warp_trident"), WarpTridentComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(MOVING_FORWARD, MovingForwardComponent::new, RespawnCopyStrategy.NEVER_COPY);
		registry.registerForPlayers(ACCELERATION, AccelerationComponent::new, RespawnCopyStrategy.NEVER_COPY);
		registry.registerFor(MobEntity.class, FROZEN, FrozenComponent::new);
		registry.registerFor(TridentEntity.class, WARP_TRIDENT, WarpTridentComponent::new);
	}
}
