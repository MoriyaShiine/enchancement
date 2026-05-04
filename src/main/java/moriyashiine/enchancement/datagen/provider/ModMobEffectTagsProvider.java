/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.datagen.provider;

import moriyashiine.enchancement.common.tag.ModMobEffectTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.resources.Identifier.parse;

public class ModMobEffectTagsProvider extends FabricTagsProvider.FabricIntrinsicHolderTagsProvider<MobEffect> {
	public ModMobEffectTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, Registries.MOB_EFFECT, registriesFuture, effect -> ResourceKey.create(Registries.MOB_EFFECT, BuiltInRegistries.MOB_EFFECT.getKey(effect)));
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		valueLookupBuilder(ModMobEffectTags.CHAOS_UNCHOOSABLE)
				.add(MobEffects.BLINDNESS.value())
				.add(MobEffects.DARKNESS.value())
				.add(MobEffects.LEVITATION.value())
				.add(MobEffects.BAD_OMEN.value())
				.add(MobEffects.RAID_OMEN.value())
				.add(MobEffects.TRIAL_OMEN.value())
				.add(MobEffects.HERO_OF_THE_VILLAGE.value());
		builder(ModMobEffectTags.CHAOS_UNCHOOSABLE)
				.addOptional(key("spectrum:ascension"))
				.addOptional(key("spectrum:divinity"));
	}

	private static ResourceKey<MobEffect> key(String id) {
		return ResourceKey.create(Registries.MOB_EFFECT, parse(id));
	}
}
