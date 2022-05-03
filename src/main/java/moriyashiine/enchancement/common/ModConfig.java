/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.util.Identifier;

import java.util.List;

@Config(name = Enchancement.MOD_ID)
public class ModConfig implements ConfigData {
	@ConfigEntry.Gui.Excluded
	public List<Identifier> allowedEnchantmentIdentifiers;

	@ConfigEntry.Gui.Excluded
	public List<Identifier> tableExcludedEnchantmentIdentifiers;

	@ConfigEntry.Gui.Excluded
	public List<String> allowedEnchantments = List.of(
			"enchancement:assimilation", "enchancement:buffet", "enchancement:perception",
			"enchancement:amphibious", "enchancement:strafe", "enchancement:wardenspine",
			"enchancement:dash", "enchancement:impact", "enchancement:slide",
			"enchancement:acceleration", "enchancement:bouncy", "enchancement:gale",
			"enchancement:berserk", "minecraft:fire_aspect", "enchancement:frostbite",
			"minecraft:infinity",
			"enchancement:chaos", "enchancement:delay", "enchancement:phasing",
			"enchancement:brimstone", "enchancement:homing", "enchancement:torch",
			"minecraft:channeling", "enchancement:leech", "minecraft:riptide", "enchancement:warp",
			"enchancement:extracting", "enchancement:molten",
			"enchancement:beheading", "enchancement:lumberjack",
			"enchancement:bury", "enchancement:scooping",
			"enchancement:disarm", "enchancement:grapple", "minecraft:luck_of_the_sea",
			"minecraft:efficiency", "minecraft:silk_touch");

	@ConfigEntry.Gui.Excluded
	public List<String> tableExcludedEnchantments = List.of();

	public boolean allowTreasureEnchantmentsInEnchantingTable = false;
	public boolean overhaulEnchantingTable = true;
	public boolean singleEnchantmentMode = true;
	@ConfigEntry.Gui.RequiresRestart
	public boolean singleLevelMode = true;

	public boolean allowInfinityOnCrossbows = true;
	public boolean allTridentsHaveLoyalty = true;
	public boolean channelingWorksWhenNotThundering = true;
	public boolean fireAspectWorksAsFlintAndSteel = true;
	public boolean luckOfTheSeaHasLure = true;
	public boolean safeChanneling = true;
	public boolean tridentsReturnFromVoid = true;

	public int maxExtractingBlocks = 64;
	public int maxLumberjackBlocks = 1024;
	public int unbreakingChangesFlag = 0;

	public boolean isEnchantmentDisallowed(Identifier identifier) {
		return !allowedEnchantmentIdentifiers.isEmpty() && !allowedEnchantmentIdentifiers.contains(identifier);
	}
}
