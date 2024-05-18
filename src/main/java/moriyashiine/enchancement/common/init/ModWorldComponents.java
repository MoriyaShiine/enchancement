/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.world.LumberjackComponent;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;

public class ModWorldComponents implements WorldComponentInitializer {
	//axe
	public static final ComponentKey<LumberjackComponent> LUMBERJACK = ComponentRegistry.getOrCreate(Enchancement.id("lumberjack"), LumberjackComponent.class);

	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		registry.register(LUMBERJACK, LumberjackComponent::new);
	}
}
