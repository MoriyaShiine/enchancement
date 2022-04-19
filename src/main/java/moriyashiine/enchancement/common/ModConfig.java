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

	@ConfigEntry.Gui.RequiresRestart
	public List<String> allowedEnchantments = List.of("enchancement:assimilation", "enchancement:buffet", "enchancement:perception",
			"enchancement:amphibious", "enchancement:wardenspine",
			"enchancement:dash", "enchancement:impact", "enchancement:slide",
			"enchancement:acceleration", "enchancement:bouncy", "enchancement:gale",
			"enchancement:berserk", "minecraft:fire_aspect", "enchancement:frostbite",
			"enchancement:chaos", "enchancement:delay", "minecraft:infinity", "enchancement:phasing",
			"minecraft:channeling", "enchancement:leech", "minecraft:riptide", "enchancement:warp",
			"enchancement:extracting", "enchancement:molten",
			"enchancement:beheading", "enchancement:lumberjack",
			"enchancement:bury", "enchancement:scooping",
			"enchancement:disarm", "enchancement:grapple", "minecraft:luck_of_the_sea",
			"minecraft:efficiency", "minecraft:silk_touch", "minecraft:unbreaking");

	public boolean singleEnchantmentMode = true;
	@ConfigEntry.Gui.RequiresRestart
	public boolean singleLevelMode = true;

	public boolean allTridentsHaveLoyalty = true;
	public boolean channelingWorksWhenNotThundering = true;
	public boolean disableChannelingFire = true;
	public boolean luckOfTheSeaHasLure = true;

	public int fireAspectIgnitionLevel = 1;
	public int maxExtractingBlocks = 64;
	public int maxLumberjackBlocks = 1024;
	@ConfigEntry.Gui.RequiresRestart
	public float armorDurabilityMultiplier = 2;
	@ConfigEntry.Gui.RequiresRestart
	public float toolDurabilityMultiplier = 2;
	@ConfigEntry.Gui.RequiresRestart
	public int fishingRodDurability = 256;
	public int unbreakableUnbreakingLevel = 1;
}
