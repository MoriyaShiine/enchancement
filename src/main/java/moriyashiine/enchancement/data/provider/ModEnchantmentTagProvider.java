/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEnchantmentTags;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTagProvider extends FabricTagProvider.EnchantmentTagProvider {
	public ModEnchantmentTagProvider(FabricDataOutput output) {
		super(output, CompletableFuture.supplyAsync(BuiltinRegistries::createWrapperLookup));
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		getOrCreateTagBuilder(ModTags.Enchantments.DISALLOWS_TOGGLEABLE_PASSIVE)
				.add(Enchantments.RIPTIDE)
				.add(ModEnchantments.LUMBERJACK);

		getOrCreateTagBuilder(ConventionalEnchantmentTags.ENTITY_AUXILIARY_MOVEMENT_ENHANCEMENTS)
				.add(ModEnchantments.STRAFE)
				.add(ModEnchantments.DASH)
				.add(ModEnchantments.SLIDE)
				.add(ModEnchantments.BUOY)
				.add(ModEnchantments.GALE);
		getOrCreateTagBuilder(ConventionalEnchantmentTags.ENTITY_DEFENSE_ENHANCEMENTS)
				.add(ModEnchantments.VEIL)
				.add(ModEnchantments.AMPHIBIOUS)
				.add(ModEnchantments.WARDENSPINE)
				.add(ModEnchantments.BOUNCY);
		getOrCreateTagBuilder(ConventionalEnchantmentTags.ENTITY_SPEED_ENHANCEMENTS)
				.add(ModEnchantments.ADRENALINE)
				.add(ModEnchantments.BUOY);
		getOrCreateTagBuilder(ConventionalEnchantmentTags.INCREASE_ENTITY_DROPS)
				.add(ModEnchantments.SCOOPING);
		getOrCreateTagBuilder(ConventionalEnchantmentTags.WEAPON_DAMAGE_ENHANCEMENTS)
				.add(ModEnchantments.BERSERK)
				.add(ModEnchantments.DELAY)
				.add(ModEnchantments.SCOOPING);
	}
}
