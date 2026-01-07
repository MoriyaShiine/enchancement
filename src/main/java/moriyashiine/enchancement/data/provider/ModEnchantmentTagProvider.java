/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.tag.ModEnchantmentTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEnchantmentTags;
import net.minecraft.data.tag.EnchantmentTagProvider;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTagProvider extends EnchantmentTagProvider {
	public static final List<Identifier> NON_TREASURE_ENCHANTMENTS = new ArrayList<>(), TREASURE_ENCHANTMENTS = new ArrayList<>();

	public ModEnchantmentTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		ProvidedTagBuilder<RegistryKey<Enchantment>, Enchantment> nonTreasure = builder(EnchantmentTags.NON_TREASURE);
		ProvidedTagBuilder<RegistryKey<Enchantment>, Enchantment> treasure = builder(EnchantmentTags.TREASURE);
		ProvidedTagBuilder<RegistryKey<Enchantment>, Enchantment> tooltipOrder = builder(EnchantmentTags.TOOLTIP_ORDER);
		NON_TREASURE_ENCHANTMENTS.forEach(id -> nonTreasure.add(RegistryKey.of(RegistryKeys.ENCHANTMENT, id)));
		TREASURE_ENCHANTMENTS.forEach(id -> treasure.add(RegistryKey.of(RegistryKeys.ENCHANTMENT, id)));
		NON_TREASURE_ENCHANTMENTS.forEach(id -> tooltipOrder.add(RegistryKey.of(RegistryKeys.ENCHANTMENT, id)));
		TREASURE_ENCHANTMENTS.forEach(id -> tooltipOrder.add(RegistryKey.of(RegistryKeys.ENCHANTMENT, id)));

		builder(ModEnchantmentTags.AUTOMATICALLY_FEEDS)
				.add(ModEnchantments.ASSIMILATION);
		builder(ModEnchantmentTags.ANIMAL_ARMOR_ENCHANTMENTS)
				.add(ModEnchantments.BOUNCY)
				.add(ModEnchantments.BUOY)
				.add(ModEnchantments.STICKY);
		builder(ModEnchantmentTags.DISALLOWS_TOGGLEABLE_PASSIVE)
				.add(Enchantments.EFFICIENCY)
				.add(Enchantments.RIPTIDE);
		builder(ModEnchantmentTags.FREEZES_ENTITIES)
				.add(ModEnchantments.FROSTBITE);

		builder(ConventionalEnchantmentTags.ENTITY_AUXILIARY_MOVEMENT_ENHANCEMENTS)
				.add(ModEnchantments.STRAFE)
				.add(ModEnchantments.DASH)
				.add(ModEnchantments.GALE)
				.add(ModEnchantments.SLIDE)
				.add(ModEnchantments.BUOY)
				.add(ModEnchantments.STICKY);
		builder(ConventionalEnchantmentTags.ENTITY_DEFENSE_ENHANCEMENTS)
				.add(ModEnchantments.VEIL)
				.add(ModEnchantments.AMPHIBIOUS)
				.add(ModEnchantments.WARDENSPINE)
				.add(ModEnchantments.BOUNCY);
		builder(ConventionalEnchantmentTags.ENTITY_SPEED_ENHANCEMENTS)
				.add(ModEnchantments.ADRENALINE)
				.add(ModEnchantments.BUOY);
		builder(ConventionalEnchantmentTags.INCREASE_ENTITY_DROPS)
				.add(ModEnchantments.SCOOPING);
		builder(ConventionalEnchantmentTags.WEAPON_DAMAGE_ENHANCEMENTS)
				.add(ModEnchantments.BERSERK)
				.add(ModEnchantments.DELAY)
				.add(ModEnchantments.SCOOPING)
				.add(ModEnchantments.APEX);

		builder(ModEnchantmentTags.BOUNCY_EXCLUSIVE_SET)
				.add(Enchantments.FEATHER_FALLING);
		builder(ModEnchantmentTags.BRIMSTONE_EXCLUSIVE_SET)
				.addTag(ModEnchantmentTags.UNIQUE_CROSSBOW_PROJECTILE_EXCLUSIVE_SET)
				.add(Enchantments.PIERCING);
		builder(ModEnchantmentTags.FROSTBITE_EXCLUSIVE_SET)
				.add(Enchantments.FIRE_ASPECT);
		builder(ModEnchantmentTags.MACE_EXCLUSIVE_SET)
				.add(ModEnchantments.METEOR)
				.add(ModEnchantments.THUNDERSTRUCK)
				.add(Enchantments.WIND_BURST);
		builder(ModEnchantmentTags.SILK_TOUCH_EXCLUSIVE_SET)
				.add(Enchantments.SILK_TOUCH);
		builder(ModEnchantmentTags.UNIQUE_CROSSBOW_PROJECTILE_EXCLUSIVE_SET)
				.add(ModEnchantments.BRIMSTONE)
				.add(ModEnchantments.SCATTER)
				.add(ModEnchantments.TORCH);
		builder(ModEnchantmentTags.WARDENSPINE_EXCLUSIVE_SET)
				.add(Enchantments.THORNS);

		builder(EnchantmentTags.RIPTIDE_EXCLUSIVE_SET)
				.add(ModEnchantments.WARP);

		// librarian trades
		addLibrarianTrades(EnchantmentTags.DESERT_COMMON_TRADE, EnchantmentTags.DESERT_SPECIAL_TRADE,
				ModEnchantments.BURY, Enchantments.FIRE_ASPECT, ModEnchantments.MOLTEN);
		addLibrarianTrades(EnchantmentTags.JUNGLE_COMMON_TRADE, EnchantmentTags.JUNGLE_SPECIAL_TRADE,
				ModEnchantments.BOUNCY, ModEnchantments.CHAOS, ModEnchantments.PERCEPTION);
		addLibrarianTrades(EnchantmentTags.PLAINS_COMMON_TRADE, EnchantmentTags.PLAINS_SPECIAL_TRADE,
				ModEnchantments.APEX, ModEnchantments.ASSIMILATION, ModEnchantments.GALE);
		addLibrarianTrades(EnchantmentTags.SAVANNA_COMMON_TRADE, EnchantmentTags.SAVANNA_SPECIAL_TRADE,
				ModEnchantments.BRIMSTONE, ModEnchantments.STICKY, ModEnchantments.TORCH);
		addLibrarianTrades(EnchantmentTags.SNOW_COMMON_TRADE, EnchantmentTags.SNOW_SPECIAL_TRADE,
				ModEnchantments.FROSTBITE, Enchantments.SILK_TOUCH, ModEnchantments.SLIDE);
		addLibrarianTrades(EnchantmentTags.SWAMP_COMMON_TRADE, EnchantmentTags.SWAMP_SPECIAL_TRADE,
				ModEnchantments.AMPHIBIOUS, ModEnchantments.BUOY, Enchantments.LUCK_OF_THE_SEA);
		addLibrarianTrades(EnchantmentTags.TAIGA_COMMON_TRADE, EnchantmentTags.TAIGA_SPECIAL_TRADE,
				ModEnchantments.GRAPPLE, ModEnchantments.LUMBERJACK, ModEnchantments.PHASING);
	}

	@SafeVarargs
	private void addLibrarianTrades(TagKey<Enchantment> common, TagKey<Enchantment> special, RegistryKey<Enchantment>... enchantments) {
		builder(common).add(List.of(enchantments));
		builder(special).add(List.of(enchantments));
	}
}
