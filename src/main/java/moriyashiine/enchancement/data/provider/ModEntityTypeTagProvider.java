/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.init.ModEntityTypes;
import moriyashiine.enchancement.common.tag.ModEntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
	public ModEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		getOrCreateTagBuilder(ModEntityTypeTags.BRIMSTONE_HITTABLE)
				.addOptionalTag(ConventionalEntityTypeTags.BOATS)
				.addOptionalTag(ConventionalEntityTypeTags.MINECARTS)
				.add(EntityType.END_CRYSTAL);
		getOrCreateTagBuilder(ModEntityTypeTags.BYPASSES_DECREASING_DAMAGE)
				.add(ModEntityTypes.AMETHYST_SHARD)
				.add(ModEntityTypes.ICE_SHARD)
				.add(ModEntityTypes.TORCH);
		getOrCreateTagBuilder(ModEntityTypeTags.CANNOT_BURY)
				.addOptionalTag(ConventionalEntityTypeTags.BOSSES)
				.add(EntityType.CREAKING)
				.add(EntityType.ELDER_GUARDIAN)
				.add(EntityType.WARDEN)
				.add(EntityType.VEX);
		getOrCreateTagBuilder(ModEntityTypeTags.CANNOT_DISARM)
				.addOptionalTag(ConventionalEntityTypeTags.BOSSES);
		getOrCreateTagBuilder(ModEntityTypeTags.CANNOT_FREEZE)
				.addOptionalTag(ConventionalEntityTypeTags.BOSSES)
				.addOptionalTag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES);
		getOrCreateTagBuilder(ModEntityTypeTags.VEIL_IMMUNE)
				.addOptionalTag(ConventionalEntityTypeTags.BOSSES)
				.add(EntityType.ELDER_GUARDIAN)
				.add(EntityType.WARDEN);
	}
}
