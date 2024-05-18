/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common;

import eu.midnightdust.lib.config.MidnightConfig;
import moriyashiine.enchancement.client.util.AllowDuplicateKeybindingsMode;

import java.util.Arrays;
import java.util.List;

public class ModConfig extends MidnightConfig {
	@Entry
	public static List<String> disallowedEnchantments = Arrays.asList(
			"minecraft:aqua_affinity",
			"minecraft:bane_of_arthropods",
			"minecraft:binding_curse",
			"minecraft:blast_protection",
			"minecraft:depth_strider",
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
			"minecraft:vanishing_curse",
			"spectrum:indestructible",
			"spectrum:autosmelt");
	@Entry
	public static boolean invertedList = false;

	@Entry
	public static boolean overhaulEnchantingTable = true;
	@Entry
	public static boolean allowTreasureEnchantmentsInEnchantingTable = false;

	@Entry
	public static boolean singleLevelMode = true;
	@Entry
	public static int enchantmentLimit = 1;

	@Entry
	public static boolean accurateFishingBobbers = true;
	@Entry
	public static boolean arrowsDropOnHit = true;
	@Entry
	public static boolean crossbowsPullFromInventory = true;
	@Entry
	public static boolean disableDurability = true;
	@Entry
	public static boolean drownedUseHeldTrident = true;
	@Entry
	public static boolean enchantedChestplatesIncreaseAirMobility = true;
	@Entry
	public static boolean fasterBows = true;
	@Entry
	public static boolean freeEnchantedBookMerging = true;
	@Entry
	public static boolean luckOfTheSeaHasLure = true;
	@Entry
	public static boolean negateEnderPearlDamage = true;
	@Entry
	public static boolean projectilesBypassCooldown = true;
	@Entry
	public static boolean projectilesNegateVelocity = true;
	@Entry
	public static boolean randomMobEnchantments = true;
	@Entry
	public static boolean rebalanceArmor = true;
	@Entry
	public static boolean rebalanceChanneling = true;
	@Entry
	public static boolean rebalanceFireAspect = true;
	@Entry
	public static boolean rebalanceGoldenApples = true;
	@Entry
	public static boolean rebalancePotions = true;
	@Entry
	public static boolean tridentsHaveInnateLoyalty = true;
	@Entry
	public static boolean tridentsReturnFromVoid = true;

	@Entry(min = 0, max = 1)
	public static float weaponEnchantmentCooldownRequirement = 0.7F;

	@Entry(min = 1)
	public static int maxExtractingBlocks = 64;
	@Entry(min = 1)
	public static int maxLumberjackBlocks = 1024;
	@Entry(min = 1)
	public static int maxLumberjackHorizontalLength = 7;
	@Entry(min = 0)
	public static int coyoteBiteTicks = 3;

	@Entry(category = "client")
	public static boolean enchantmentDescriptions = true;
	@Entry(category = "client")
	public static boolean coloredEnchantmentNames = true;
	@Entry(category = "client")
	public static AllowDuplicateKeybindingsMode allowDuplicateKeybindings = AllowDuplicateKeybindingsMode.VANILLA_AND_ENCHANCEMENT;
	@Entry(category = "client")
	public static boolean singlePressStrafe = false;

	public static int encode() {
		StringBuilder builder = new StringBuilder();
		for (String value : disallowedEnchantments) {
			builder.append(value);
		}
		String encoding = builder.toString() +
				invertedList +
				overhaulEnchantingTable + allowTreasureEnchantmentsInEnchantingTable +
				singleLevelMode + enchantmentLimit +
				accurateFishingBobbers + arrowsDropOnHit +
				crossbowsPullFromInventory +
				disableDurability + drownedUseHeldTrident +
				enchantedChestplatesIncreaseAirMobility +
				fasterBows + freeEnchantedBookMerging +
				luckOfTheSeaHasLure +
				negateEnderPearlDamage +
				projectilesBypassCooldown + projectilesNegateVelocity +
				randomMobEnchantments + rebalanceArmor + rebalanceChanneling + rebalanceFireAspect + rebalanceGoldenApples + rebalancePotions +
				tridentsHaveInnateLoyalty + tridentsReturnFromVoid +
				weaponEnchantmentCooldownRequirement +
				maxExtractingBlocks + maxLumberjackBlocks + maxLumberjackHorizontalLength +
				coyoteBiteTicks;
		return encoding.hashCode();
	}

	static {
		MidnightConfig.init(Enchancement.MOD_ID, ModConfig.class);
	}
}
