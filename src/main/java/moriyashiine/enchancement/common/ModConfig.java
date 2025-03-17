/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common;

import eu.midnightdust.lib.config.MidnightConfig;
import moriyashiine.enchancement.client.util.AllowDuplicateKeybindingsMode;
import moriyashiine.enchancement.common.util.OverhaulMode;

import java.util.Arrays;
import java.util.List;

public class ModConfig extends MidnightConfig {
	@Entry
	public static List<String> disallowedEnchantments = Arrays.asList(
			"minecraft:aqua_affinity",
			"minecraft:bane_of_arthropods",
			"minecraft:binding_curse",
			"minecraft:blast_protection",
			"minecraft:breach",
			"minecraft:density",
			"minecraft:depth_strider",
			"minecraft:efficiency",
			"minecraft:feather_falling",
			"minecraft:fire_protection",
			"minecraft:flame",
			"minecraft:fortune",
			"minecraft:frost_walker",
			"minecraft:impaling",
			"minecraft:infinity",
			"minecraft:knockback",
			"minecraft:looting",
			"minecraft:loyalty",
			"minecraft:lure",
			"minecraft:mending",
			"minecraft:multishot",
			"minecraft:piercing",
			"minecraft:power",
			"minecraft:projectile_protection",
			"minecraft:protection",
			"minecraft:punch",
			"minecraft:quick_charge",
			"minecraft:respiration",
			"minecraft:sharpness",
			"minecraft:smite",
			"minecraft:soul_speed",
			"minecraft:sweeping_edge",
			"minecraft:swift_sneak",
			"minecraft:thorns",
			"minecraft:unbreaking",
			"minecraft:vanishing_curse");
	@Entry
	public static boolean invertedList = false;

	@Entry
	public static OverhaulMode overhaulEnchantingTable = OverhaulMode.ACCEPTABLE;

	@Entry
	public static boolean singleLevelMode = true;
	@Entry
	public static int enchantmentLimit = 1;

	@Entry
	public static boolean disableDurability = true;
	@Entry
	public static boolean disableVelocityChecks = true;
	@Entry
	public static boolean enhanceMobs = true;
	@Entry
	public static boolean rebalanceConsumables = true;
	@Entry
	public static boolean rebalanceEnchantments = true;
	@Entry
	public static boolean rebalanceEquipment = true;
	@Entry
	public static boolean rebalanceProjectiles = true;
	@Entry
	public static boolean rebalanceStatusEffects = true;
	@Entry
	public static boolean toggleablePassives = true;

	@Entry(min = 0, max = 1)
	public static float weaponEnchantmentCooldownRequirement = 0.7F;

	@Entry(min = 1)
	public static int maxFellTreesBlocks = 1024;
	@Entry(min = 1)
	public static int maxFellTreesHorizontalLength = 7;
	@Entry(min = 1)
	public static int maxMineOreVeinsBlocks = 64;
	@Entry(min = 0)
	public static int coyoteBiteTicks = 3;

	@Entry(category = "client")
	public static boolean enchantmentDescriptions = true;
	@Entry(category = "client")
	public static boolean coloredEnchantmentNames = true;
	@Entry(category = "client")
	public static AllowDuplicateKeybindingsMode allowDuplicateKeybindings = AllowDuplicateKeybindingsMode.VANILLA_AND_ENCHANCEMENT;
	@Entry(category = "client")
	public static boolean invertedBounce = false;
	@Entry(category = "client")
	public static boolean doublePressDirectionBurst = false;
	@Entry(category = "client")
	public static boolean inputlessDirectionBurst = false;

	public static int encode() {
		StringBuilder builder = new StringBuilder();
		for (String value : disallowedEnchantments) {
			builder.append(value);
		}
		String encoding = builder.toString() +
				invertedList +
				overhaulEnchantingTable +
				singleLevelMode + enchantmentLimit +
				disableDurability + disableVelocityChecks +
				enhanceMobs +
				rebalanceConsumables + rebalanceEnchantments + rebalanceEquipment + rebalanceProjectiles + rebalanceStatusEffects +
				toggleablePassives +
				weaponEnchantmentCooldownRequirement +
				maxFellTreesBlocks + maxFellTreesHorizontalLength + maxMineOreVeinsBlocks +
				coyoteBiteTicks;
		return encoding.hashCode();
	}

	static {
		// need to do this before mod init since I mixin into certain things before then
		MidnightConfig.init(Enchancement.MOD_ID, ModConfig.class);
	}
}
