/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.tag;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class EnchancementBlockTags {
	public static final TagKey<Block> BURIABLE = TagKey.create(Registries.BLOCK, Enchancement.id("buriable"));
	public static final TagKey<Block> FELLABLE = TagKey.create(Registries.BLOCK, Enchancement.id("fellable"));
	public static final TagKey<Block> SMELTS_SELF = TagKey.create(Registries.BLOCK, Enchancement.id("smelts_self"));
	public static final TagKey<Block> UNSTICKABLE = TagKey.create(Registries.BLOCK, Enchancement.id("unstickable"));

	public static final TagKey<Block> DEEPSLATE_BASE_BLOCKS = TagKey.create(Registries.BLOCK, Enchancement.id("base_blocks/deepslate"));
	public static final TagKey<Block> NETHERRACK_BASE_BLOCKS = TagKey.create(Registries.BLOCK, Enchancement.id("base_blocks/netherrack"));
}
