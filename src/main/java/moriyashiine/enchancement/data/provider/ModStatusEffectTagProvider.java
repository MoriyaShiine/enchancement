/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.tag.ModStatusEffectTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.util.Identifier.of;

public class ModStatusEffectTagProvider extends FabricTagProvider.FabricValueLookupTagProvider<StatusEffect> {
	public ModStatusEffectTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, RegistryKeys.STATUS_EFFECT, registriesFuture, statusEffect -> RegistryKey.of(RegistryKeys.STATUS_EFFECT, Registries.STATUS_EFFECT.getId(statusEffect)));
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		valueLookupBuilder(ModStatusEffectTags.CHAOS_UNCHOOSABLE)
				.add(StatusEffects.BLINDNESS.value())
				.add(StatusEffects.DARKNESS.value())
				.add(StatusEffects.LEVITATION.value())
				.add(StatusEffects.BAD_OMEN.value())
				.add(StatusEffects.RAID_OMEN.value())
				.add(StatusEffects.TRIAL_OMEN.value())
				.add(StatusEffects.HERO_OF_THE_VILLAGE.value());
		builder(ModStatusEffectTags.CHAOS_UNCHOOSABLE)
				.addOptional(key("spectrum:ascension"))
				.addOptional(key("spectrum:divinity"));
	}

	private static RegistryKey<StatusEffect> key(String id) {
		return RegistryKey.of(RegistryKeys.STATUS_EFFECT, of(id));
	}
}
