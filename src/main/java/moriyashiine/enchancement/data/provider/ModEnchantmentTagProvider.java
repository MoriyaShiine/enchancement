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
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTagProvider extends FabricTagProvider.EnchantmentTagProvider {
	public static final List<Identifier> NON_TREASURE_ENCHANTMENTS = new ArrayList<>(), TREASURE_ENCHANTMENTS = new ArrayList<>();

	public ModEnchantmentTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		FabricTagProvider<Enchantment>.FabricTagBuilder nonTreasure = getOrCreateTagBuilder(EnchantmentTags.NON_TREASURE);
		FabricTagProvider<Enchantment>.FabricTagBuilder treasure = getOrCreateTagBuilder(EnchantmentTags.TREASURE);
		FabricTagProvider<Enchantment>.FabricTagBuilder tooltipOrder = getOrCreateTagBuilder(EnchantmentTags.TOOLTIP_ORDER);
		NON_TREASURE_ENCHANTMENTS.forEach(nonTreasure::add);
		TREASURE_ENCHANTMENTS.forEach(treasure::add);
		NON_TREASURE_ENCHANTMENTS.forEach(tooltipOrder::add);
		TREASURE_ENCHANTMENTS.forEach(tooltipOrder::add);

		getOrCreateTagBuilder(ModEnchantmentTags.AUTOMATICALLY_FEEDS)
				.add(ModEnchantments.ASSIMILATION);
		getOrCreateTagBuilder(ModEnchantmentTags.ANIMAL_ARMOR_ENCHANTMENTS)
				.add(ModEnchantments.BOUNCY)
				.add(ModEnchantments.BUOY)
				.add(ModEnchantments.STICKY);
		getOrCreateTagBuilder(ModEnchantmentTags.DISALLOWS_TOGGLEABLE_PASSIVE)
				.add(Enchantments.EFFICIENCY)
				.add(Enchantments.RIPTIDE);
		getOrCreateTagBuilder(ModEnchantmentTags.FREEZES_ENTITIES)
				.add(ModEnchantments.FROSTBITE);

		getOrCreateTagBuilder(ConventionalEnchantmentTags.ENTITY_AUXILIARY_MOVEMENT_ENHANCEMENTS)
				.add(ModEnchantments.STRAFE)
				.add(ModEnchantments.DASH)
				.add(ModEnchantments.GALE)
				.add(ModEnchantments.SLIDE)
				.add(ModEnchantments.BUOY)
				.add(ModEnchantments.STICKY);
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
				.add(ModEnchantments.SCOOPING)
				.add(ModEnchantments.APEX);

		getOrCreateTagBuilder(ModEnchantmentTags.BOUNCY_EXCLUSIVE_SET)
				.add(Enchantments.FEATHER_FALLING);
		getOrCreateTagBuilder(ModEnchantmentTags.BRIMSTONE_EXCLUSIVE_SET)
				.add(Enchantments.PIERCING);
		getOrCreateTagBuilder(ModEnchantmentTags.FROSTBITE_EXCLUSIVE_SET)
				.add(Enchantments.FIRE_ASPECT);
		getOrCreateTagBuilder(ModEnchantmentTags.MACE_EXCLUSIVE_SET)
				.add(ModEnchantments.METEOR)
				.add(ModEnchantments.THUNDERSTRUCK)
				.add(Enchantments.WIND_BURST);
		getOrCreateTagBuilder(ModEnchantmentTags.SILK_TOUCH_EXCLUSIVE_SET)
				.add(Enchantments.SILK_TOUCH);
		getOrCreateTagBuilder(ModEnchantmentTags.WARDENSPINE_EXCLUSIVE_SET)
				.add(Enchantments.THORNS);

		getOrCreateTagBuilder(EnchantmentTags.RIPTIDE_EXCLUSIVE_SET)
				.add(ModEnchantments.WARP);
	}
}
