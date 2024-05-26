/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.init.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModStatusEffectTagProvider extends FabricTagProvider<StatusEffect> {
	public ModStatusEffectTagProvider(FabricDataOutput output) {
		super(output, RegistryKeys.STATUS_EFFECT, CompletableFuture.supplyAsync(BuiltinRegistries::createWrapperLookup));
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		getOrCreateTagBuilder(ModTags.StatusEffects.CHAOS_UNCHOOSABLE)
				.add(StatusEffects.BAD_OMEN.value())
				.add(StatusEffects.RAID_OMEN.value())
				.add(StatusEffects.TRIAL_OMEN.value())
				.add(StatusEffects.HERO_OF_THE_VILLAGE.value())
				.addOptional(Identifier.tryParse("spectrum:ascension"))
				.addOptional(Identifier.tryParse("spectrum:divinity"))
				.addOptional(Identifier.tryParse("bewitchment:mortal_coil"))
				.addOptional(Identifier.tryParse("bewitchment:wednesday"));
	}
}
