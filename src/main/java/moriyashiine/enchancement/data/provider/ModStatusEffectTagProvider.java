/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.tag.ModStatusEffectTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.util.Identifier.of;

public class ModStatusEffectTagProvider extends FabricTagProvider<StatusEffect> {
	public ModStatusEffectTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, RegistryKeys.STATUS_EFFECT, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		builder(ModStatusEffectTags.CHAOS_UNCHOOSABLE)
				.add(key(StatusEffects.BLINDNESS))
				.add(key(StatusEffects.DARKNESS))
				.add(key(StatusEffects.LEVITATION))
				.add(key(StatusEffects.BAD_OMEN))
				.add(key(StatusEffects.RAID_OMEN))
				.add(key(StatusEffects.TRIAL_OMEN))
				.add(key(StatusEffects.HERO_OF_THE_VILLAGE))
				.addOptional(key(of("spectrum", "ascension")))
				.addOptional(key(of("spectrum", "divinity")));
	}

	private static RegistryKey<StatusEffect> key(RegistryEntry<StatusEffect> entry) {
		return entry.getKey().orElseThrow();
	}

	private static RegistryKey<StatusEffect> key(Identifier id) {
		return RegistryKey.of(RegistryKeys.STATUS_EFFECT, id);
	}
}
