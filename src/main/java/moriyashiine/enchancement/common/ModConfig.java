/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.Arrays;
import java.util.List;

public class ModConfig extends MidnightConfig {
	@Entry
	public static List<String> allowedEnchantments = Arrays.asList(
			"enchancement:assimilation", "enchancement:perception", "enchancement:veil",
			"enchancement:amphibious", "enchancement:strafe", "enchancement:wardenspine",
			"enchancement:dash", "enchancement:slide",
			"enchancement:bouncy", "enchancement:buoy", "enchancement:gale",
			"enchancement:berserk", "minecraft:fire_aspect", "enchancement:frostbite",
			"minecraft:infinity",
			"enchancement:chaos", "enchancement:delay", "enchancement:phasing",
			"enchancement:brimstone", "enchancement:scatter", "enchancement:torch",
			"minecraft:channeling", "enchancement:leech", "minecraft:riptide", "enchancement:warp",
			"enchancement:extracting", "enchancement:molten",
			"enchancement:beheading", "enchancement:lumberjack",
			"enchancement:bury", "enchancement:scooping",
			"enchancement:disarm", "enchancement:grapple", "minecraft:luck_of_the_sea",
			"minecraft:efficiency", "minecraft:silk_touch");
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
	public static boolean allowInfinityOnCrossbows = true;
	@Entry
	public static boolean allTridentsHaveLoyalty = true;
	@Entry
	public static boolean channelingIgnitesOnMelee = true;
	@Entry
	public static boolean channelingWorksWhenNotThundering = true;
	@Entry
	public static boolean fireAspectWorksAsFlintAndSteel = true;
	@Entry
	public static boolean freeEnchantedBookMerging = true;
	@Entry
	public static boolean luckOfTheSeaHasLure = true;
	@Entry
	public static boolean negateEnderPearlDamage = true;
	@Entry
	public static boolean safeChanneling = true;
	@Entry
	public static boolean tridentsReturnFromVoid = true;
	@Entry
	public static boolean weakerFireAspect = true;
	@Entry
	public static boolean weakerGoldenApple = true;
	@Entry
	public static boolean weakerPotions = true;

	@Entry(min = 0, max = 1)
	public static float weaponEnchantmentCooldownRequirement = 0.7F;

	@Entry(min = 1)
	public static int maxExtractingBlocks = 64;
	@Entry(min = 1)
	public static int maxLumberjackBlocks = 1024;
	@Entry(min = 1)
	public static int maxLumberjackHorizontalLength = 7;
	@Entry(min = -1)
	public static int unbreakingChangesFlag = 0;

	@Entry(category = "client")
	public static boolean enchantmentDescriptions = true;

	public static int encode() {
		StringBuilder builder = new StringBuilder();
		for (String value : allowedEnchantments) {
			builder.append(value);
		}
		String encoding = builder.toString() +
				invertedList +
				overhaulEnchantingTable + allowTreasureEnchantmentsInEnchantingTable +
				singleLevelMode + enchantmentLimit +
				allowInfinityOnCrossbows + allTridentsHaveLoyalty +
				channelingIgnitesOnMelee + channelingWorksWhenNotThundering +
				fireAspectWorksAsFlintAndSteel + freeEnchantedBookMerging +
				luckOfTheSeaHasLure +
				negateEnderPearlDamage +
				safeChanneling +
				tridentsReturnFromVoid +
				weakerFireAspect + weakerGoldenApple + weakerPotions +
				weaponEnchantmentCooldownRequirement +
				maxExtractingBlocks + maxLumberjackBlocks + maxLumberjackHorizontalLength +
				unbreakingChangesFlag;
		return encoding.hashCode();
	}

	static {
		MidnightConfig.init(Enchancement.MOD_ID, ModConfig.class);
	}
}
