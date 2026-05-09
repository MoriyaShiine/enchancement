/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.datagen.provider;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.tag.ModEnchantmentTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEnchantmentTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTagsProvider extends FabricTagsProvider<Enchantment> {
	public static final List<Identifier> NON_TREASURE_ENCHANTMENTS = new ArrayList<>(), TREASURE_ENCHANTMENTS = new ArrayList<>();

	public ModEnchantmentTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, Registries.ENCHANTMENT, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		TagAppender<ResourceKey<Enchantment>, Enchantment> nonTreasure = builder(EnchantmentTags.NON_TREASURE);
		TagAppender<ResourceKey<Enchantment>, Enchantment> treasure = builder(EnchantmentTags.TREASURE);
		TagAppender<ResourceKey<Enchantment>, Enchantment> tooltipOrder = builder(EnchantmentTags.TOOLTIP_ORDER);
		NON_TREASURE_ENCHANTMENTS.forEach(id -> nonTreasure.add(ResourceKey.create(Registries.ENCHANTMENT, id)));
		TREASURE_ENCHANTMENTS.forEach(id -> treasure.add(ResourceKey.create(Registries.ENCHANTMENT, id)));
		NON_TREASURE_ENCHANTMENTS.forEach(id -> tooltipOrder.add(ResourceKey.create(Registries.ENCHANTMENT, id)));
		TREASURE_ENCHANTMENTS.forEach(id -> tooltipOrder.add(ResourceKey.create(Registries.ENCHANTMENT, id)));

		builder(ModEnchantmentTags.ANIMAL_ARMOR_ENCHANTMENTS)
				.add(ModEnchantments.ADRENALINE)
				.add(ModEnchantments.AMPHIBIOUS)
				.add(ModEnchantments.STRAFE)
				.add(ModEnchantments.WARDENSPINE);
		builder(ModEnchantmentTags.AUTOMATICALLY_FEEDS)
				.add(ModEnchantments.ASSIMILATION);
		builder(ModEnchantmentTags.DISALLOWS_TOGGLEABLE_PASSIVE)
				.add(Enchantments.EFFICIENCY)
				.add(Enchantments.RIPTIDE);
		builder(ModEnchantmentTags.FREEZES_ENTITIES)
				.add(ModEnchantments.FROSTBITE);
		builder(ModEnchantmentTags.SADDLE_ENCHANTMENTS)
				.add(ModEnchantments.BOUNCY)
				.add(ModEnchantments.BUOY)
				.add(ModEnchantments.STICKY);

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

		builder(EnchantmentTags.RIPTIDE_EXCLUSIVE)
				.add(ModEnchantments.WARP);

		// librarian trades
		addLibrarianTrades(EnchantmentTags.TRADES_DESERT_COMMON,
				ModEnchantments.BURY, Enchantments.FIRE_ASPECT, ModEnchantments.TORCH);
		addLibrarianTrades(EnchantmentTags.TRADES_JUNGLE_COMMON,
				ModEnchantments.BOUNCY, ModEnchantments.CHAOS, ModEnchantments.PERCEPTION);
		addLibrarianTrades(EnchantmentTags.TRADES_PLAINS_COMMON,
				ModEnchantments.APEX, ModEnchantments.ASSIMILATION, ModEnchantments.GALE);
		addLibrarianTrades(EnchantmentTags.TRADES_SAVANNA_COMMON,
				ModEnchantments.BRIMSTONE, ModEnchantments.STICKY, ModEnchantments.VEIL);
		addLibrarianTrades(EnchantmentTags.TRADES_SNOW_COMMON,
				ModEnchantments.FROSTBITE, Enchantments.SILK_TOUCH, ModEnchantments.SLIDE);
		addLibrarianTrades(EnchantmentTags.TRADES_SWAMP_COMMON,
				ModEnchantments.AMPHIBIOUS, ModEnchantments.BUOY, Enchantments.LUCK_OF_THE_SEA);
		addLibrarianTrades(EnchantmentTags.TRADES_TAIGA_COMMON,
				ModEnchantments.GRAPPLE, ModEnchantments.LUMBERJACK, ModEnchantments.PHASING);
	}

	@SafeVarargs
	private void addLibrarianTrades(TagKey<Enchantment> common, ResourceKey<Enchantment>... enchantments) {
		builder(common).add(enchantments);
	}
}
