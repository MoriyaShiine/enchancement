/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.datagen.provider;

import moriyashiine.enchancement.common.init.EnchancementEnchantments;
import moriyashiine.enchancement.common.tag.EnchancementEnchantmentTags;
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

public class EnchancementEnchantmentTagsProvider extends FabricTagsProvider<Enchantment> {
	public static final List<Identifier> NON_TREASURE_ENCHANTMENTS = new ArrayList<>(), TREASURE_ENCHANTMENTS = new ArrayList<>();

	public EnchancementEnchantmentTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, Registries.ENCHANTMENT, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider registries) {
		TagAppender<Enchantment> nonTreasure = builder(EnchantmentTags.NON_TREASURE);
		TagAppender<Enchantment> treasure = builder(EnchantmentTags.TREASURE);
		TagAppender<Enchantment> tooltipOrder = builder(EnchantmentTags.TOOLTIP_ORDER);
		NON_TREASURE_ENCHANTMENTS.forEach(id -> nonTreasure.add(ResourceKey.create(Registries.ENCHANTMENT, id)));
		TREASURE_ENCHANTMENTS.forEach(id -> treasure.add(ResourceKey.create(Registries.ENCHANTMENT, id)));
		NON_TREASURE_ENCHANTMENTS.forEach(id -> tooltipOrder.add(ResourceKey.create(Registries.ENCHANTMENT, id)));
		TREASURE_ENCHANTMENTS.forEach(id -> tooltipOrder.add(ResourceKey.create(Registries.ENCHANTMENT, id)));

		builder(EnchancementEnchantmentTags.ANIMAL_ARMOR_ENCHANTMENTS)
				.add(EnchancementEnchantments.ADRENALINE)
				.add(EnchancementEnchantments.AMPHIBIOUS)
				.add(EnchancementEnchantments.STRAFE)
				.add(EnchancementEnchantments.WARDENSPINE);
		builder(EnchancementEnchantmentTags.AUTOMATICALLY_FEEDS)
				.add(EnchancementEnchantments.ASSIMILATION);
		builder(EnchancementEnchantmentTags.DISALLOWS_TOGGLEABLE_PASSIVE)
				.add(Enchantments.EFFICIENCY)
				.add(Enchantments.RIPTIDE);
		builder(EnchancementEnchantmentTags.FREEZES_ENTITIES)
				.add(EnchancementEnchantments.FROSTBITE);
		builder(EnchancementEnchantmentTags.SADDLE_ENCHANTMENTS)
				.add(EnchancementEnchantments.BOUNCY)
				.add(EnchancementEnchantments.BUOY)
				.add(EnchancementEnchantments.E_SPEED)
				.add(EnchancementEnchantments.STICKY);

		builder(ConventionalEnchantmentTags.ENTITY_AUXILIARY_MOVEMENT_ENHANCEMENTS)
				.add(EnchancementEnchantments.STRAFE)
				.add(EnchancementEnchantments.DASH)
				.add(EnchancementEnchantments.GALE)
				.add(EnchancementEnchantments.SLIDE)
				.add(EnchancementEnchantments.BOUNCY)
				.add(EnchancementEnchantments.BUOY)
				.add(EnchancementEnchantments.E_SPEED)
				.add(EnchancementEnchantments.STICKY);
		builder(ConventionalEnchantmentTags.ENTITY_DEFENSE_ENHANCEMENTS)
				.add(EnchancementEnchantments.AMPHIBIOUS)
				.add(EnchancementEnchantments.WARDENSPINE)
				.add(EnchancementEnchantments.BOUNCY);
		builder(ConventionalEnchantmentTags.ENTITY_SPEED_ENHANCEMENTS)
				.add(EnchancementEnchantments.ADRENALINE)
				.add(EnchancementEnchantments.AMPHIBIOUS)
				.add(EnchancementEnchantments.BUOY)
				.add(EnchancementEnchantments.E_SPEED);
		builder(ConventionalEnchantmentTags.INCREASE_ENTITY_DROPS)
				.add(EnchancementEnchantments.SCOOPING);
		builder(ConventionalEnchantmentTags.WEAPON_DAMAGE_ENHANCEMENTS)
				.add(EnchancementEnchantments.BERSERK)
				.add(EnchancementEnchantments.BURROWING)
				.add(EnchancementEnchantments.BEHEADING)
				.add(EnchancementEnchantments.SCOOPING)
				.add(EnchancementEnchantments.APEX);
		builder(ConventionalEnchantmentTags.HIDDEN_FROM_RECIPE_VIEWERS)
				.add(EnchancementEnchantments.EMPTY_KEY);

		builder(EnchancementEnchantmentTags.BOUNCY_EXCLUSIVE_SET)
				.add(Enchantments.FEATHER_FALLING);
		builder(EnchancementEnchantmentTags.BRIMSTONE_EXCLUSIVE_SET)
				.addTag(EnchancementEnchantmentTags.UNIQUE_CROSSBOW_PROJECTILE_EXCLUSIVE_SET)
				.add(Enchantments.PIERCING);
		builder(EnchancementEnchantmentTags.FROSTBITE_EXCLUSIVE_SET)
				.add(Enchantments.FIRE_ASPECT);
		builder(EnchancementEnchantmentTags.MACE_EXCLUSIVE_SET)
				.add(EnchancementEnchantments.METEOR)
				.add(EnchancementEnchantments.THUNDERSTRUCK)
				.add(Enchantments.WIND_BURST);
		builder(EnchancementEnchantmentTags.SILK_TOUCH_EXCLUSIVE_SET)
				.add(Enchantments.SILK_TOUCH);
		builder(EnchancementEnchantmentTags.UNIQUE_CROSSBOW_PROJECTILE_EXCLUSIVE_SET)
				.add(EnchancementEnchantments.BRIMSTONE)
				.add(EnchancementEnchantments.SCATTER)
				.add(EnchancementEnchantments.TORCH);
		builder(EnchancementEnchantmentTags.WARDENSPINE_EXCLUSIVE_SET)
				.add(Enchantments.THORNS);

		builder(EnchantmentTags.RIPTIDE_EXCLUSIVE)
				.add(EnchancementEnchantments.WARP);

		// librarian trades
		addLibrarianTrades(EnchantmentTags.TRADES_DESERT_COMMON,
				EnchancementEnchantments.SCOOPING, Enchantments.FIRE_ASPECT, EnchancementEnchantments.TORCH);
		addLibrarianTrades(EnchantmentTags.TRADES_JUNGLE_COMMON,
				EnchancementEnchantments.BOUNCY, EnchancementEnchantments.CHAOS, EnchancementEnchantments.PERCEPTION);
		addLibrarianTrades(EnchantmentTags.TRADES_PLAINS_COMMON,
				EnchancementEnchantments.APEX, EnchancementEnchantments.ASSIMILATION, EnchancementEnchantments.GALE);
		addLibrarianTrades(EnchantmentTags.TRADES_SAVANNA_COMMON,
				EnchancementEnchantments.BRIMSTONE, EnchancementEnchantments.STICKY, EnchancementEnchantments.VEIL);
		addLibrarianTrades(EnchantmentTags.TRADES_SNOW_COMMON,
				EnchancementEnchantments.FROSTBITE, Enchantments.SILK_TOUCH, EnchancementEnchantments.SLIDE);
		addLibrarianTrades(EnchantmentTags.TRADES_SWAMP_COMMON,
				EnchancementEnchantments.AMPHIBIOUS, EnchancementEnchantments.BUOY, Enchantments.LUCK_OF_THE_SEA);
		addLibrarianTrades(EnchantmentTags.TRADES_TAIGA_COMMON,
				EnchancementEnchantments.GRAPPLE, EnchancementEnchantments.LUMBERJACK, EnchancementEnchantments.PHASING);
	}

	@SafeVarargs
	private void addLibrarianTrades(TagKey<Enchantment> common, ResourceKey<Enchantment>... enchantments) {
		builder(common).add(enchantments);
	}
}
