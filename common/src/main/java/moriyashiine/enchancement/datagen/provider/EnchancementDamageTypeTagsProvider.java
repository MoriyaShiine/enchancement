package moriyashiine.enchancement.datagen.provider;

import moriyashiine.enchancement.common.init.EnchancementDamageTypes;
import moriyashiine.enchancement.common.tag.EnchancementDamageTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

import java.util.concurrent.CompletableFuture;

public class EnchancementDamageTypeTagsProvider extends FabricTagsProvider<DamageType> {
	public EnchancementDamageTypeTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, Registries.DAMAGE_TYPE, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		builder(EnchancementDamageTypeTags.BYPASSES_WARDENSPINE)
				.forceAddTag(DamageTypeTags.AVOIDS_GUARDIAN_THORNS)
				.forceAddTag(DamageTypeTags.BYPASSES_INVULNERABILITY);
		builder(EnchancementDamageTypeTags.DOES_NOT_INTERRUPT)
				.forceAddTag(DamageTypeTags.BYPASSES_INVULNERABILITY)
				.add(EnchancementDamageTypes.LIFE_DRAIN);
		builder(EnchancementDamageTypeTags.IS_SAFE_FALL)
				.add(DamageTypes.ENDER_PEARL)
				.add(DamageTypes.FALL);

		builder(DamageTypeTags.BYPASSES_ARMOR)
				.add(EnchancementDamageTypes.BRIMSTONE)
				.add(EnchancementDamageTypes.LIFE_DRAIN);
		builder(DamageTypeTags.BYPASSES_COOLDOWN)
				.add(EnchancementDamageTypes.AMETHYST_SHARD)
				.add(EnchancementDamageTypes.ICE_SHARD);
		builder(DamageTypeTags.BYPASSES_ENCHANTMENTS)
				.add(EnchancementDamageTypes.BRIMSTONE)
				.add(EnchancementDamageTypes.LIFE_DRAIN);
		builder(DamageTypeTags.BYPASSES_RESISTANCE)
				.add(EnchancementDamageTypes.BRIMSTONE)
				.add(EnchancementDamageTypes.LIFE_DRAIN);
		builder(DamageTypeTags.IS_FREEZING)
				.add(EnchancementDamageTypes.ICE_SHARD);
		builder(DamageTypeTags.IS_PROJECTILE)
				.add(EnchancementDamageTypes.AMETHYST_SHARD)
				.add(EnchancementDamageTypes.BRIMSTONE)
				.add(EnchancementDamageTypes.ICE_SHARD);
		builder(DamageTypeTags.NO_IMPACT)
				.add(EnchancementDamageTypes.LIFE_DRAIN);
		builder(DamageTypeTags.NO_KNOCKBACK)
				.add(EnchancementDamageTypes.LIFE_DRAIN);
	}
}
