/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.tag.ModStatusEffectTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.util.Identifier.of;

public class ModStatusEffectTagProvider extends FabricTagProvider<StatusEffect> {
	public ModStatusEffectTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, RegistryKeys.STATUS_EFFECT, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		getOrCreateTagBuilder(ModStatusEffectTags.CHAOS_UNCHOOSABLE)
				.add(StatusEffects.BLINDNESS.value())
				.add(StatusEffects.DARKNESS.value())
				.add(StatusEffects.BAD_OMEN.value())
				.add(StatusEffects.RAID_OMEN.value())
				.add(StatusEffects.TRIAL_OMEN.value())
				.add(StatusEffects.HERO_OF_THE_VILLAGE.value())
				.addOptional(of("bewitchment", "mortal_coil"))
				.addOptional(of("bewitchment", "wednesday"))
				.addOptional(of("spectrum", "ascension"))
				.addOptional(of("spectrum", "divinity"));
	}
}
