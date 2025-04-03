/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.block.ChiseledBookshelfComponent;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import org.ladysnake.cca.api.v3.block.BlockComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.block.BlockComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

public class ModBlockComponents implements BlockComponentInitializer {
	public static final ComponentKey<ChiseledBookshelfComponent> CHISELED_BOOKSHELF = ComponentRegistry.getOrCreate(Enchancement.id("chiseled_bookshelf"), ChiseledBookshelfComponent.class);

	@Override
	public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
		registry.registerFor(ChiseledBookshelfBlockEntity.class, CHISELED_BOOKSHELF, ChiseledBookshelfComponent::new);
	}
}
