/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.util.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public record BaseBlock(Block base) {
	public static final Codec<BaseBlock> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("base").forGetter(BaseBlock::base)
	).apply(instance, BaseBlock::new));

	public static final Map<Block, BaseBlock> BLOCK_MAP = new HashMap<>();
}
