/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.datagen.provider;

import moriyashiine.enchancement.common.tag.EnchancementMobEffectTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.resources.Identifier.parse;

public class EnchancementMobEffectTagsProvider extends FabricTagsProvider<MobEffect> {
	public EnchancementMobEffectTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, Registries.MOB_EFFECT, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		add(EnchancementMobEffectTags.CHAOS_UNCHOOSABLE,
				MobEffects.BLINDNESS,
				MobEffects.DARKNESS,
				MobEffects.LEVITATION,
				MobEffects.BAD_OMEN,
				MobEffects.RAID_OMEN,
				MobEffects.TRIAL_OMEN,
				MobEffects.HERO_OF_THE_VILLAGE);
		builder(EnchancementMobEffectTags.CHAOS_UNCHOOSABLE)
				.addOptional(key("spectrum:ascension"))
				.addOptional(key("spectrum:divinity"));
	}

	@SafeVarargs
	private void add(TagKey<MobEffect> tagKey, Holder<MobEffect>... effects) {
		TagAppender<ResourceKey<MobEffect>, MobEffect> builder = builder(tagKey);
		for (Holder<MobEffect> effect : effects) {
			builder.add(effect.unwrapKey().orElseThrow());
		}
	}

	private static ResourceKey<MobEffect> key(String id) {
		return ResourceKey.create(Registries.MOB_EFFECT, parse(id));
	}
}
