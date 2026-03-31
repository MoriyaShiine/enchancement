/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
	public static final TagKey<Block> BURIABLE = TagKey.create(Registries.BLOCK, Enchancement.id("buriable"));
	public static final TagKey<Block> END_ORES = TagKey.create(Registries.BLOCK, Enchancement.id("end_ores"));
	public static final TagKey<Block> NETHER_ORES = TagKey.create(Registries.BLOCK, Enchancement.id("nether_ores"));
	public static final TagKey<Block> SMELTS_SELF = TagKey.create(Registries.BLOCK, Enchancement.id("smelts_self"));
	public static final TagKey<Block> UNSTICKABLE = TagKey.create(Registries.BLOCK, Enchancement.id("unstickable"));
}
