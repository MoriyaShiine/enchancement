/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.world.FellTreesComponent;
import moriyashiine.enchancement.common.component.world.HoneyTrailComponent;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;

public class ModWorldComponents implements WorldComponentInitializer {
	public static final ComponentKey<FellTreesComponent> FELL_TREES = ComponentRegistry.getOrCreate(Enchancement.id("fell_trees"), FellTreesComponent.class);
	public static final ComponentKey<HoneyTrailComponent> HONEY_TRAIL = ComponentRegistry.getOrCreate(Enchancement.id("honey_trail"), HoneyTrailComponent.class);

	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		registry.register(FELL_TREES, FellTreesComponent::new);
		registry.register(HONEY_TRAIL, HoneyTrailComponent::new);
	}
}
