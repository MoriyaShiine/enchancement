/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.registry;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.world.LumberjackComponent;
import net.minecraft.util.Identifier;

public class ModWorldComponents implements WorldComponentInitializer {
	//axe
	public static final ComponentKey<LumberjackComponent> LUMBERJACK = ComponentRegistry.getOrCreate(new Identifier(Enchancement.MOD_ID, "lumberjack"), LumberjackComponent.class);

	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		registry.register(LUMBERJACK, LumberjackComponent::new);
	}
}
