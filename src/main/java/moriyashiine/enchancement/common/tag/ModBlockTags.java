/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModBlockTags {
	public static final TagKey<Block> BURIABLE = TagKey.of(RegistryKeys.BLOCK, Enchancement.id("buriable"));
	public static final TagKey<Block> END_ORES = TagKey.of(RegistryKeys.BLOCK, Enchancement.id("end_ores"));
	public static final TagKey<Block> NETHER_ORES = TagKey.of(RegistryKeys.BLOCK, Enchancement.id("nether_ores"));
	public static final TagKey<Block> SMELTS_SELF = TagKey.of(RegistryKeys.BLOCK, Enchancement.id("smelts_self"));
	public static final TagKey<Block> UNSTICKABLE = TagKey.of(RegistryKeys.BLOCK, Enchancement.id("unstickable"));
}
