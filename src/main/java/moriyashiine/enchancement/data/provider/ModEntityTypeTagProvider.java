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
		valueLookupBuilder(ModEntityTypeTags.BRIMSTONE_HITTABLE)
				.forceAddTag(ConventionalEntityTypeTags.BOATS)
				.forceAddTag(ConventionalEntityTypeTags.MINECARTS)
				.add(EntityType.END_CRYSTAL);
		valueLookupBuilder(ModEntityTypeTags.BYPASSES_DECREASING_DAMAGE)
				.add(ModEntityTypes.AMETHYST_SHARD)
				.add(ModEntityTypes.ICE_SHARD)
				.add(ModEntityTypes.TORCH);
		valueLookupBuilder(ModEntityTypeTags.CANNOT_BURY)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES)
				.add(EntityType.CREAKING)
				.add(EntityType.ELDER_GUARDIAN)
				.add(EntityType.WARDEN)
				.add(EntityType.VEX);
		valueLookupBuilder(ModEntityTypeTags.CANNOT_DISARM)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES);
		valueLookupBuilder(ModEntityTypeTags.CANNOT_FREEZE)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES)
				.forceAddTag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES);
		valueLookupBuilder(ModEntityTypeTags.VEIL_IMMUNE)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES)
				.add(EntityType.ELDER_GUARDIAN)
				.add(EntityType.WARDEN);
	}
}
