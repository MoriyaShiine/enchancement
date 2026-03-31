/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.level.FellTreesComponent;
import moriyashiine.enchancement.common.component.level.HoneyTrailComponent;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v8.level.LevelComponentFactoryRegistry;
import org.ladysnake.cca.api.v8.level.LevelComponentInitializer;

public class ModLevelComponents implements LevelComponentInitializer {
	public static final ComponentKey<FellTreesComponent> FELL_TREES = ComponentRegistry.getOrCreate(Enchancement.id("fell_trees"), FellTreesComponent.class);
	public static final ComponentKey<HoneyTrailComponent> HONEY_TRAIL = ComponentRegistry.getOrCreate(Enchancement.id("honey_trail"), HoneyTrailComponent.class);

	@Override
	public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
		registry.register(FELL_TREES, FellTreesComponent::new);
		registry.register(HONEY_TRAIL, HoneyTrailComponent::new);
	}
}
