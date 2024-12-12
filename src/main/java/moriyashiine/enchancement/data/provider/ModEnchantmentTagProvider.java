/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.tag.ModEnchantmentTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEnchantmentTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTagProvider extends FabricTagProvider.EnchantmentTagProvider {
	public static final List<Identifier> ALL_ENCHANCEMENT_ENCHANTMENTS = new ArrayList<>();

	public ModEnchantmentTagProvider(FabricDataOutput output) {
		super(output, CompletableFuture.supplyAsync(BuiltinRegistries::createWrapperLookup));
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		FabricTagProvider<Enchantment>.FabricTagBuilder nonTreasure = getOrCreateTagBuilder(EnchantmentTags.NON_TREASURE);
		FabricTagProvider<Enchantment>.FabricTagBuilder tooltipOrder = getOrCreateTagBuilder(EnchantmentTags.TOOLTIP_ORDER);
		ALL_ENCHANCEMENT_ENCHANTMENTS.forEach(nonTreasure::addOptional);
		ALL_ENCHANCEMENT_ENCHANTMENTS.forEach(tooltipOrder::addOptional);

		getOrCreateTagBuilder(ModEnchantmentTags.AUTOMATICALLY_FEEDS)
				.addOptional(ModEnchantments.ASSIMILATION);
		getOrCreateTagBuilder(ModEnchantmentTags.DISALLOWS_TOGGLEABLE_PASSIVE)
				.addOptional(Enchantments.EFFICIENCY)
				.addOptional(Enchantments.RIPTIDE);
		getOrCreateTagBuilder(ModEnchantmentTags.FREEZES_ENTITIES)
				.addOptional(ModEnchantments.FROSTBITE);

		getOrCreateTagBuilder(ConventionalEnchantmentTags.ENTITY_AUXILIARY_MOVEMENT_ENHANCEMENTS)
				.addOptional(ModEnchantments.STRAFE)
				.addOptional(ModEnchantments.DASH)
				.addOptional(ModEnchantments.GALE)
				.addOptional(ModEnchantments.SLIDE)
				.addOptional(ModEnchantments.BUOY)
				.addOptional(ModEnchantments.STICKY);
		getOrCreateTagBuilder(ConventionalEnchantmentTags.ENTITY_DEFENSE_ENHANCEMENTS)
				.addOptional(ModEnchantments.VEIL)
				.addOptional(ModEnchantments.AMPHIBIOUS)
				.addOptional(ModEnchantments.WARDENSPINE)
				.addOptional(ModEnchantments.BOUNCY);
		getOrCreateTagBuilder(ConventionalEnchantmentTags.ENTITY_SPEED_ENHANCEMENTS)
				.addOptional(ModEnchantments.ADRENALINE)
				.addOptional(ModEnchantments.BUOY);
		getOrCreateTagBuilder(ConventionalEnchantmentTags.INCREASE_ENTITY_DROPS)
				.addOptional(ModEnchantments.SCOOPING);
		getOrCreateTagBuilder(ConventionalEnchantmentTags.WEAPON_DAMAGE_ENHANCEMENTS)
				.addOptional(ModEnchantments.BERSERK)
				.addOptional(ModEnchantments.DELAY)
				.addOptional(ModEnchantments.SCOOPING);

		getOrCreateTagBuilder(ModEnchantmentTags.BOUNCY_EXCLUSIVE_SET)
				.addOptional(Enchantments.FEATHER_FALLING);
		getOrCreateTagBuilder(ModEnchantmentTags.BRIMSTONE_EXCLUSIVE_SET)
				.addOptional(Enchantments.PIERCING);
		getOrCreateTagBuilder(ModEnchantmentTags.FROSTBITE_EXCLUSIVE_SET)
				.addOptional(Enchantments.FIRE_ASPECT);
		getOrCreateTagBuilder(ModEnchantmentTags.MACE_EXCLUSIVE_SET)
				.addOptional(ModEnchantments.METEOR)
				.addOptional(ModEnchantments.THUNDERSTRUCK)
				.addOptional(Enchantments.WIND_BURST);
		getOrCreateTagBuilder(ModEnchantmentTags.SILK_TOUCH_EXCLUSIVE_SET)
				.addOptional(Enchantments.SILK_TOUCH);
		getOrCreateTagBuilder(ModEnchantmentTags.WARDENSPINE_EXCLUSIVE_SET)
				.addOptional(Enchantments.THORNS);

		getOrCreateTagBuilder(EnchantmentTags.RIPTIDE_EXCLUSIVE_SET)
				.addOptional(ModEnchantments.WARP);
	}
}
