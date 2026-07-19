package moriyashiine.enchancement.datagen.provider;

import moriyashiine.enchancement.common.init.EnchancementEntityTypes;
import moriyashiine.enchancement.common.tag.EnchancementEntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class EnchancementEntityTypeTagsProvider extends FabricTagsProvider.EntityTypeTagsProvider {
	public EnchancementEntityTypeTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		valueLookupBuilder(EnchancementEntityTypeTags.BRIMSTONE_HITTABLE)
				.forceAddTag(ConventionalEntityTypeTags.BOATS)
				.forceAddTag(ConventionalEntityTypeTags.MINECARTS)
				.add(EntityType.END_CRYSTAL);
		valueLookupBuilder(EnchancementEntityTypeTags.BYPASSES_DECREASING_DAMAGE)
				.add(EnchancementEntityTypes.AMETHYST_SHARD)
				.add(EnchancementEntityTypes.ICE_SHARD)
				.add(EnchancementEntityTypes.TORCH);
		valueLookupBuilder(EnchancementEntityTypeTags.CANNOT_BURY)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES)
				.add(EntityType.CREAKING)
				.add(EntityType.ELDER_GUARDIAN)
				.add(EntityType.WARDEN)
				.add(EntityType.VEX);
		valueLookupBuilder(EnchancementEntityTypeTags.CANNOT_DISARM)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES);
		valueLookupBuilder(EnchancementEntityTypeTags.CANNOT_FLUID_WALK)
				.forceAddTag(EntityTypeTags.CAN_WEAR_NAUTILUS_ARMOR);
		valueLookupBuilder(EnchancementEntityTypeTags.CANNOT_FREEZE)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES)
				.forceAddTag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES);
		valueLookupBuilder(EnchancementEntityTypeTags.VEIL_IMMUNE)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES)
				.add(EntityType.ELDER_GUARDIAN)
				.add(EntityType.WARDEN);
	}
}
