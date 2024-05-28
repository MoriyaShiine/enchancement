package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

public class ModBlockTags {
	public static final TagKey<Block> BURIABLE = TagKey.of(Registries.BLOCK.getKey(), Enchancement.id("buriable"));
	public static final TagKey<Block> END_ORES = TagKey.of(Registries.BLOCK.getKey(), Enchancement.id("end_ores"));
	public static final TagKey<Block> NETHER_ORES = TagKey.of(Registries.BLOCK.getKey(), Enchancement.id("nether_ores"));
	public static final TagKey<Block> SMELTS_SELF = TagKey.of(Registries.BLOCK.getKey(), Enchancement.id("smelts_self"));
}
